package pro.progr.saveanowl.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pro.progr.diamondapi.AuthInterface

class VkAuthViewModel(
    private val auth: AuthInterface,
    private val api: AuthApi // твой Retrofit
) : ViewModel() {

    private val _ui = MutableStateFlow<AuthUiState>(AuthUiState.LoggedOut)
    val ui = _ui.asStateFlow()

    init {
        // при старте подтягиваем текущее состояние из хранилища
        viewModelScope.launch {
            auth.isAuthorized().collect { ok ->
                _ui.value = if (ok) AuthUiState.LoggedIn(auth.getName()) else AuthUiState.LoggedOut
            }
        }
    }

    fun signIn() {
        _ui.value = AuthUiState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            VKID.instance.authorize(
                callback = object : VKIDAuthCallback {
                    override fun onAuth(accessToken: AccessToken) {
                        viewModelScope.launch(Dispatchers.IO) {
                            runCatching {
                                val deviceId = auth.getDeviceId() // уже из Auth
                                val resp = api.signIn(AuthVkRequest(
                                    vkAccessToken = accessToken.token,
                                    deviceId = deviceId
                                ))
                                if (!resp.isSuccessful) error("HTTP ${resp.code()} ${resp.message()}")
                                resp.body() ?: error("Empty body")
                            }.onSuccess { body ->
                                // 1) Сохраняем в безопасное хранилище
                                auth.setSessionId(body.sessionId)
                                auth.setSessionSecret(body.sessionSecret)
                                auth.setName(body.name)

                                // 2) Обновляем UI
                                _ui.value = AuthUiState.LoggedIn(body.name)

                            }.onFailure { e ->
                                _ui.value = AuthUiState.Error(e.message ?: "Auth failed")
                            }
                        }
                    }

                    override fun onFail(fail: VKIDAuthFail) {
                        _ui.value = AuthUiState.Error(
                            when (fail) {
                                is VKIDAuthFail.Canceled -> "Отменено"
                                else -> fail.description
                            }
                        )
                    }
                },
                params = VKIDAuthParams { }
            )
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.clearSession() // добавь в AuthInterface
            _ui.value = AuthUiState.LoggedOut
        }
    }
}

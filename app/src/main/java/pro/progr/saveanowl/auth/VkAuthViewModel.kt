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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pro.progr.diamondapi.AuthInterface

class VkAuthViewModel(
    private val auth: AuthInterface,
    private val api: AuthApi
) : ViewModel() {

    private val signingIn = MutableStateFlow(false)
    private val errorMsg  = MutableStateFlow<String?>(null)

    // Единственный источник правды: строим UI из auth.isAuthorized()
    val ui: StateFlow<AuthUiState> = combine(
        auth.isAuthorized().distinctUntilChanged(),
        signingIn,
        errorMsg
    ) { authorized, signing, err ->
        when {
            err != null     -> AuthUiState.Error(err)
            signing         -> AuthUiState.Loading
            authorized      -> AuthUiState.LoggedIn(auth.getName())
            else            -> AuthUiState.LoggedOut
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, AuthUiState.Loading)

    fun signIn() {
        // если уже авторизован — чистим
        if (auth.getSessionId() != null)  auth.clearSession()

        errorMsg.value = null
        signingIn.value = true

        viewModelScope.launch(Dispatchers.Default) {
            VKID.instance.authorize(
                callback = object : VKIDAuthCallback {
                    override fun onAuth(accessToken: AccessToken) {
                        viewModelScope.launch(Dispatchers.IO) {
                            runCatching {
                                val resp = api.signIn(
                                    AuthVkRequest(
                                        vkAccessToken = accessToken.token,
                                        deviceId = auth.getDeviceId()
                                    )
                                )
                                if (!resp.isSuccessful) error("HTTP ${resp.code()} ${resp.message()}")
                                resp.body() ?: error("Empty body")
                            }.onSuccess { body ->
                                // сохраняем только через Auth — UI сам обновится из потока
                                auth.setSession(sessionId = body.sessionId,
                                    sessionSecret = body.sessionSecret)
                                auth.setName(body.name)
                                errorMsg.value = null
                            }.onFailure { e ->
                                errorMsg.value = e.message ?: "Auth failed"
                            }.also {
                                signingIn.value = false
                            }
                        }
                    }
                    override fun onFail(fail: VKIDAuthFail) {
                        errorMsg.value = when (fail) {
                            is VKIDAuthFail.Canceled -> "Отменено"
                            else -> fail.description
                        }
                        signingIn.value = false
                    }
                },
                params = VKIDAuthParams { }
            )
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        auth.clearSession()
        errorMsg.value = null
    }
}

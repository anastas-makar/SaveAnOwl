package pro.progr.saveanowl.vk

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.VKID
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pro.progr.todos.util.DeviceIdProvider

class VkAuthViewModel : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    fun signIn() {
        _state.value = AuthState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            VKID.instance.authorize(
                callback = object : VKIDAuthCallback {
                    override fun onAuth(accessToken: AccessToken) {
                        // получили VK токен — сразу шлём на сервер
                        val token = accessToken.token
                        Log.i(TAG, "VK access_token=$token")

                        viewModelScope.launch(Dispatchers.IO) {
                            val deviceId = DeviceIdProvider.get()
                            runCatching {
                                val resp = AuthApiProvider.api.signIn(
                                    AuthVkRequest(vkAccessToken = token, deviceId = deviceId)
                                )
                                if (!resp.isSuccessful) {
                                    throw IllegalStateException("HTTP ${resp.code()} ${resp.message()}")
                                }
                                resp.body() ?: throw IllegalStateException("Empty body")
                            }.onSuccess { body ->
                                // тут можно сохранить sessionId для дальнейших запросов
                                Log.i(TAG, "sessionId=${body.sessionId}, user=${body.appUserId}")
                                _state.value = AuthState.Success(
                                    sessionId = body.sessionId,
                                    appUserId = body.appUserId
                                )
                            }.onFailure { e ->
                                Log.e(TAG, "Auth API error", e)
                                _state.value = AuthState.Error(e.message ?: "Auth failed")
                            }
                        }
                    }

                    override fun onFail(fail: VKIDAuthFail) {
                        when (fail) {
                            is VKIDAuthFail.Canceled -> Log.w(TAG, "VK auth canceled")
                            else -> Log.e(TAG, "VK auth error: $fail")
                        }

                        _state.value = AuthState.Error(fail.description)
                    }
                },
                params = VKIDAuthParams {
                }
            )

        }
    }

    companion object {
        private const val TAG = "VK_AUTH"
    }
}
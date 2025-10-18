package pro.progr.saveanowl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.VKID
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VkAuthViewModel : ViewModel() {

    fun signIn() {
        viewModelScope.launch(Dispatchers.Default) {
            VKID.instance.authorize(
                callback = object : VKIDAuthCallback {
                    override fun onAuth(accessToken: AccessToken) {
                        // Тут пока только логируем, потом пошлёшь на сервер
                        Log.i(TAG, "VK access_token=${accessToken.token}")
                    }

                    override fun onFail(fail: VKIDAuthFail) {
                        when (fail) {
                            is VKIDAuthFail.Canceled -> Log.w(TAG, "VK auth canceled")
                            else -> Log.e(TAG, "VK auth error: $fail")
                        }
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
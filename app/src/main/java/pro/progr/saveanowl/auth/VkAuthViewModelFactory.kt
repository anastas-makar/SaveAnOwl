package pro.progr.saveanowl.auth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VkAuthViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val auth = Auth(app.applicationContext)          // твоя реализация AuthInterface
        val api  = AuthApiProvider.api                   // как у тебя сейчас
        return VkAuthViewModel(auth, api) as T
    }
}
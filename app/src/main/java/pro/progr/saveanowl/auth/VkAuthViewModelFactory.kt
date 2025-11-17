package pro.progr.saveanowl.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pro.progr.saveanowl.SaveAnOwlApplication

class VkAuthViewModelFactory(
    private val app: SaveAnOwlApplication
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val auth = app.auth          // твоя реализация AuthInterface
        val api  = AuthApiProvider.api                   // как у тебя сейчас
        return VkAuthViewModel(auth, api) as T
    }
}
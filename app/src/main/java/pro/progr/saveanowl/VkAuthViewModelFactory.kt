package pro.progr.saveanowl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pro.progr.authvk.VkAuthViewModel

class VkAuthViewModelFactory(
    private val app: SaveAnOwlApplication
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VkAuthViewModel(
            auth = app.auth,
            api = app.authApi
        ) as T
    }
}
package pro.progr.saveanowl.vk

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val sessionId: String, val appUserId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
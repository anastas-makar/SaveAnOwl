package pro.progr.saveanowl.vk

data class VkAuthResponse(
    val appUserId: String,
    val sessionId: String,
    val expiresAt: String,
    val isNewUser: Boolean
)
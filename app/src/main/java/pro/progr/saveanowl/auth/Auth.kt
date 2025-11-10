package pro.progr.saveanowl.auth

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import pro.progr.diamondapi.AuthInterface

class Auth(context: Context) : AuthInterface {
    private val rnd = SecureRandom()
    private val store = SecureStore(context.applicationContext)

    private val _authorized = MutableStateFlow(store.getSessionId() != null)
    override fun isAuthorized(): StateFlow<Boolean> = _authorized

    init { DeviceIdProvider.init(context.applicationContext) }

    override fun getDeviceId(): String = DeviceIdProvider.get()

    override fun getSessionId(): String? = store.getSessionId()

    override fun setSessionId(sessionId: String) {
        store.putSessionId(sessionId)
        _authorized.value = true
    }

    override fun setSessionSecret(sessionSecret: String) {
        store.putSessionSecretB64u(sessionSecret) // хранится шифр-текст
    }

    override fun clearSession() {
        store.clearAll()
        _authorized.value = false
    }

    override fun getName(): String? = store.getDisplayName()
    override fun setName(name: String?) = store.putDisplayName(name)

    override fun getNonce(): String {
        val b = ByteArray(16); rnd.nextBytes(b)
        return Base64.encodeToString(b, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    override fun getHash(
        sessionId: String,
        deviceId: String,
        nonce: String,
        bodyBytes: ByteArray
    ): String {
        val secretRaw = store.getSessionSecretRaw()
            ?: error("No sessionSecret") // обработай выше как разлогин

        val bodySha = MessageDigest.getInstance("SHA-256").digest(bodyBytes)
        val canonical = buildString {
            append("sid=").append(sessionId).append('\n')
            append("did=").append(deviceId).append('\n')
            append("nonce=").append(nonce).append('\n')
        }.toByteArray(Charsets.US_ASCII)
        val toSign = canonical + bodySha

        val mac = Mac.getInstance("HmacSHA256").apply {
            init(SecretKeySpec(secretRaw, "HmacSHA256"))
        }
        val sig = mac.doFinal(toSign)
        return Base64.encodeToString(sig, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }
}

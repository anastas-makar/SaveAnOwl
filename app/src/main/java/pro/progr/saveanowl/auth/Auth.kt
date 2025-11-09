package pro.progr.saveanowl.auth

import android.content.Context
import kotlinx.coroutines.flow.Flow
import pro.progr.diamondapi.AuthInterface

class Auth(context: Context) : AuthInterface {

    init {
        DeviceIdProvider.init(context)
    }

    override fun clearSession() {
        TODO("Not yet implemented")
    }

    override fun getDeviceId(): String {
        return DeviceIdProvider.get()
    }

    override fun getHash(
        sessionId: String,
        deviceId: String,
        nonce: String,
        bodyBytes: ByteArray
    ): String {
        TODO("Not yet implemented")
    }

    override fun getName(): String? {
        TODO("Not yet implemented")
    }

    override fun getNonce(): String {
        TODO("Not yet implemented")
    }

    override fun getSessionId(): String? {
        return "test" //todo:
    }

    override fun getSessionSecret(): String? {
        TODO("Not yet implemented")
    }

    override fun isAuthorized(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun setName(name: String?) {
        TODO("Not yet implemented")
    }

    override fun setSessionId(sessionId: String) {
        TODO("Not yet implemented")
    }

    override fun setSessionSecret(sessionSecret: String) {
        TODO("Not yet implemented")
    }
}
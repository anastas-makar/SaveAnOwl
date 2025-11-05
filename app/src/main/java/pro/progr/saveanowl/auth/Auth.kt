package pro.progr.saveanowl.auth

import android.content.Context
import kotlinx.coroutines.flow.Flow
import pro.progr.diamondapi.AuthInterface

class Auth(context: Context) : AuthInterface {

    init {
        DeviceIdProvider.init(context)
    }
    override fun getDeviceId(): String {
        return DeviceIdProvider.get()
    }

    override fun <T> getHash(content: T): String {
        TODO("Not yet implemented")
    }

    override fun getName(): String? {
        TODO("Not yet implemented")
    }

    override fun getSessionId(): String? {
        return "test" //todo:
    }

    override fun isAuthorized(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}
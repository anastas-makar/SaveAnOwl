package pro.progr.saveanowl.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import pro.progr.authvk.Auth
import pro.progr.owlgame.worker.doGameSyncWork

class AuthorizedGameSyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val auth = Auth(applicationContext)

        if (!auth.isAuthorized().first()) {
            return Result.success()
        }

        return doGameSyncWork(
            applicationContext = applicationContext,
            auth = auth
        )
    }
}

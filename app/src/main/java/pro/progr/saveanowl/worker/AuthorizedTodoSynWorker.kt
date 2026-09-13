package pro.progr.saveanowl.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import pro.progr.authvk.Auth
import pro.progr.saveanowl.SaveAnOwlApplication
import pro.progr.todos.work.doTodoSyncWork

class AuthorizedTodoSynWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as SaveAnOwlApplication
        val auth = Auth(applicationContext)

        if (!auth.isAuthorized().first()) {
            return Result.success()
        }

        return doTodoSyncWork(applicationContext, auth, app.personalCrypto)
    }
}

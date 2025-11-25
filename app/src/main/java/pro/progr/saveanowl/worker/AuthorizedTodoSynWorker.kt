package pro.progr.saveanowl.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import pro.progr.authvk.Auth
import pro.progr.todos.work.doTodoSyncWork

class AuthorizedTodoSynWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return doTodoSyncWork(applicationContext, Auth(applicationContext))
    }

}
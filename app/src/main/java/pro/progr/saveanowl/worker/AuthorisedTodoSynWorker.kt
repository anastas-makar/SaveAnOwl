package pro.progr.saveanowl.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import pro.progr.saveanowl.auth.Auth
import pro.progr.todos.work.doTodoSyncWork

class AuthorisedTodoSynWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return doTodoSyncWork(applicationContext, Auth(applicationContext))
    }

}
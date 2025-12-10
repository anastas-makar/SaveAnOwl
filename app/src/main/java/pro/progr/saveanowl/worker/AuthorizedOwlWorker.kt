package pro.progr.saveanowl.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import pro.progr.authvk.Auth
import pro.progr.owlgame.worker.doAnimalBuildingsWork

class AuthorizedOwlWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return doAnimalBuildingsWork(applicationContext, Auth(applicationContext))
    }
}
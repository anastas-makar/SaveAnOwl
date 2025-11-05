package pro.progr.saveanowl.auth

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import pro.progr.diamondapi.AuthInterface
import pro.progr.owlgame.worker.AnimalBuildingsWorker
import pro.progr.todos.work.SyncWorker

class AuthWorkerFactory(
    private val auth: AuthInterface
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        params: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        SyncWorker::class.java.name ->
            SyncWorker(appContext, params, auth)
        AnimalBuildingsWorker::class.java.name ->
            AnimalBuildingsWorker(appContext, params) //todo: там тоже auth понадобится
        else -> null // для остальных — по необходимости
    }
}
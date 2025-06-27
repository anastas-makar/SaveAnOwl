package pro.progr.saveanowl

import android.app.Application
import pro.progr.owlgame.worker.GameWorkerSetup

class SaveAnOwlApplication : Application() {

    val appComponent: SaveAnOwlComponent by lazy {
        DaggerSaveAnOwlComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)

        /*        val workRequest = PeriodicWorkRequestBuilder<HistoryWorker>(8, TimeUnit.HOURS)
                    .build()

                WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "dailyWork",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )*/

        //запуск workmanager для игрового модуля
        GameWorkerSetup.scheduleWork(baseContext)
    }
}
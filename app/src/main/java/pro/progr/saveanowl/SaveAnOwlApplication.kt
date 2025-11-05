package pro.progr.saveanowl

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.WorkerFactory
import pro.progr.owlgame.worker.GameWorkerSetup
import pro.progr.saveanowl.auth.Auth
import pro.progr.saveanowl.auth.AuthWorkerFactory
import pro.progr.todos.dagger2.AppModule
import pro.progr.todos.dagger2.DaggerTodosComponent
import pro.progr.todos.dagger2.TodosComponent
import pro.progr.todos.work.SyncWorkerSetup

class SaveAnOwlApplication : Application(), DefaultLifecycleObserver, Configuration.Provider {

    private lateinit var workerFactory: WorkerFactory

    val appComponent: SaveAnOwlComponent by lazy {
        DaggerSaveAnOwlComponent.factory().create(applicationContext)
    }

    val todosComponent: TodosComponent by lazy {
        DaggerTodosComponent.builder()
            .application(this)                       // @BindsInstance
            .auth(Auth(this))
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super<Application>.onCreate()

        workerFactory = AuthWorkerFactory(Auth(this))

        appComponent.inject(this)
        GameWorkerSetup.scheduleWork(baseContext)

        // Подписываемся на жизненный цикл процесса, чтобы отловить уход в фон
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    // Приложение ушло в фон (все активити стали STOPPED)
    override fun onStop(owner: LifecycleOwner) {
        // Запускаем фоновый синк через твой helper из модуля todos
        SyncWorkerSetup.enqueueBackgroundSync(applicationContext)
    }

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

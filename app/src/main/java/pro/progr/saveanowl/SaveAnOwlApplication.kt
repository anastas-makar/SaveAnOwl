package pro.progr.saveanowl

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import pro.progr.owlgame.worker.GameWorkerSetup
import pro.progr.authvk.Auth
import pro.progr.authvk.AuthApiProvider
import pro.progr.saveanowl.worker.AuthorizedOwlWorker
import pro.progr.saveanowl.worker.AuthorizedTodoSynWorker
import pro.progr.todos.dagger2.AppModule
import pro.progr.todos.dagger2.DaggerTodosComponent
import pro.progr.todos.dagger2.TodosComponent
import pro.progr.todos.work.SyncWorkerSetup

class SaveAnOwlApplication : Application(), DefaultLifecycleObserver {

    // один-единственный auth на всё приложение
    val auth by lazy(LazyThreadSafetyMode.NONE) { Auth(this) }

    // и один-единственный API-клиент, собранный с этим auth
    val authApi by lazy(LazyThreadSafetyMode.NONE) { AuthApiProvider.api(auth) }

    val appComponent: SaveAnOwlComponent by lazy {
        DaggerSaveAnOwlComponent.factory().create(applicationContext)
    }

    val todosComponent: TodosComponent by lazy {
        DaggerTodosComponent.builder()
            .application(this)                       // @BindsInstance
            .auth(auth)
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super<Application>.onCreate()

        appComponent.inject(this)
        GameWorkerSetup.enqueueBackgroundSync<AuthorizedOwlWorker>(applicationContext)
        // Подписываемся на жизненный цикл процесса, чтобы отловить уход в фон
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    // Приложение ушло в фон (все активити стали STOPPED)
    override fun onStop(owner: LifecycleOwner) {
        // Запускаем фоновый синк через твой helper из модуля todos
        SyncWorkerSetup.enqueueBackgroundSync<AuthorizedTodoSynWorker>(applicationContext)
    }


}

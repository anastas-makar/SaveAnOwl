package pro.progr.saveanowl

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pro.progr.authvk.Auth
import pro.progr.authvk.AuthApiProvider
import pro.progr.owlgame.dagger.DaggerOwlGameComponent
import pro.progr.owlgame.dagger.OwlGameComponent
import pro.progr.owlgame.dagger.OwlGameModule
import pro.progr.owlgame.worker.GameWorkerSetup
import pro.progr.saveanowl.worker.AuthorizedGameSyncWorker
import pro.progr.saveanowl.worker.AuthorizedTodoSynWorker
import pro.progr.todos.dagger2.AppModule
import pro.progr.todos.dagger2.DaggerTodosComponent
import pro.progr.todos.dagger2.TodosComponent
import pro.progr.todos.work.SyncWorkerSetup

class SaveAnOwlApplication : Application(), DefaultLifecycleObserver {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // One auth instance for the whole application.
    val auth by lazy(LazyThreadSafetyMode.NONE) { Auth(this) }

    // One auth API client built with the same auth instance.
    val authApi by lazy(LazyThreadSafetyMode.NONE) { AuthApiProvider.api(auth) }

    val appComponent: SaveAnOwlComponent by lazy {
        DaggerSaveAnOwlComponent.factory().create(applicationContext)
    }

    val todosComponent: TodosComponent by lazy {
        DaggerTodosComponent.builder()
            .application(this)
            .auth(auth)
            .appModule(AppModule(this))
            .build()
    }

    val owlGameComponent: OwlGameComponent by lazy {
        DaggerOwlGameComponent.builder()
            .application(this)
            .appModule(OwlGameModule(this))
            .auth(auth)
            .purchaseInterface(todosComponent.purchaseInterface())
            .build()
    }

    override fun onCreate() {
        super<Application>.onCreate()

        VKID.init(this)
        appComponent.inject(this)

        // Do not enqueue authorized game work here unconditionally.
        // MainActivity starts/cancels the periodic animal check from auth state.
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    // The whole application process left the foreground.
    override fun onStop(owner: LifecycleOwner) {
        applicationScope.launch {
            if (!auth.isAuthorized().first()) {
                return@launch
            }

            // Notes keep their existing background sync behavior.
            SyncWorkerSetup.enqueueBackgroundSync<AuthorizedTodoSynWorker>(applicationContext)

            // Game module: one backup/restore pass. The worker checks auth again at execution
            // time in case the user logs out after this request has been queued.
            GameWorkerSetup.enqueueOneTimeGameSync<AuthorizedGameSyncWorker>(applicationContext)
        }
    }
}

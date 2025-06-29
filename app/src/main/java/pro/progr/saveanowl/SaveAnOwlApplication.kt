package pro.progr.saveanowl

import android.app.Application
import pro.progr.owlgame.worker.GameWorkerSetup
import pro.progr.todos.dagger2.AppModule
import pro.progr.todos.dagger2.DaggerTodosComponent
import pro.progr.todos.dagger2.TodosComponent

class SaveAnOwlApplication : Application() {

    val appComponent: SaveAnOwlComponent by lazy {
        DaggerSaveAnOwlComponent.factory().create(applicationContext)
    }

    val todosComponent: TodosComponent by lazy {
        DaggerTodosComponent.builder()
            .application(this)                       // @BindsInstance
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        GameWorkerSetup.scheduleWork(baseContext)
    }
}

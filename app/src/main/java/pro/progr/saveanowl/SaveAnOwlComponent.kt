package pro.progr.saveanowl

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [])
interface SaveAnOwlComponent {
    fun inject(saveAnOwlApplication: SaveAnOwlApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): SaveAnOwlComponent
    }

}
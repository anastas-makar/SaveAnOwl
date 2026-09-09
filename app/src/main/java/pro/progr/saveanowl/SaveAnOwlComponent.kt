package pro.progr.saveanowl

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pro.progr.personalcrypto.PersonalCrypto
import javax.inject.Singleton

@Singleton
@Component(modules = [PersonalCryptoModule::class])
interface SaveAnOwlComponent {
    fun inject(saveAnOwlApplication: SaveAnOwlApplication)
    fun personalCrypto(): PersonalCrypto

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): SaveAnOwlComponent
    }

}

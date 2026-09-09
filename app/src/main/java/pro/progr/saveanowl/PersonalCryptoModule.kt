package pro.progr.saveanowl

import android.content.Context
import dagger.Module
import dagger.Provides
import pro.progr.personalcrypto.AndroidPersonalCrypto
import pro.progr.personalcrypto.PersonalCrypto
import javax.inject.Singleton

@Module
object PersonalCryptoModule {
    @Provides
    @Singleton
    fun providePersonalCrypto(context: Context): PersonalCrypto =
        AndroidPersonalCrypto(context.applicationContext)
}

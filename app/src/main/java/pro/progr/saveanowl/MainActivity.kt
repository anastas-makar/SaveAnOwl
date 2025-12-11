package pro.progr.saveanowl

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : ComponentActivity() {
    // app-scope для фона
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as SaveAnOwlApplication
        val diamondsCountRepository =
            app.todosComponent.diamondsCountRepository()
        val todosDaggerVmFactory = (application as SaveAnOwlApplication).todosComponent.daggerViewModelFactory()

        VKID.init(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                app.auth.isAuthorized()
                    .collect { ok ->
                        if (ok) {
                            appScope.launch {
                                try {
                                    app.todosComponent.syncRepository().sync()
                                } catch (e: IOException) {
                                    Log.w("SyncRepository", "Network error during sync", e)
                                } catch (t: Throwable) {
                                    Log.e("SyncRepository", "Unexpected error during sync", t)
                                }

                            }
                        }
                    }
            }
        }

        val uri = intent.data
        val animalId = uri?.lastPathSegment

        setContent {
            AppNavigation(diamondsCountRepository = diamondsCountRepository,
                todosDaggerVmFactory = todosDaggerVmFactory,
                startDestination =
                if (animalId != null)
                    "animal?id=${Uri.encode(animalId)}"
                else
                    "todos")
        }

    }
}
package pro.progr.saveanowl

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import pro.progr.owlgame.worker.GameWorkerSetup
import pro.progr.owlgame.worker.runGameSync
import pro.progr.saveanowl.worker.AuthorizedAnimalArrivalWorker
import java.io.IOException

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as SaveAnOwlApplication
        val diamondsCountRepository = app.todosComponent.purchaseInterface()
        val todosDaggerVmFactory = app.todosComponent.daggerViewModelFactory()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                app.auth.isAuthorized().collectLatest { authorized ->
                    if (!authorized) {
                        // WorkManager tasks can survive process/activity restarts.
                        // Cancel queued game work on logout; workers also check auth themselves.
                        GameWorkerSetup.cancelPeriodicAnimalArrivalCheck(applicationContext)
                        GameWorkerSetup.cancelOneTimeGameSync(applicationContext)
                        return@collectLatest
                    }

                    // Schedule the periodic animal-arrival check only for an authorized user.
                    // KEEP prevents every foreground transition from resetting the periodic work.
                    GameWorkerSetup.enqueuePeriodicAnimalArrivalCheck<AuthorizedAnimalArrivalWorker>(
                        applicationContext
                    )

                    // Direct sync on app foreground / successful authorization.
                    // collectLatest + repeatOnLifecycle cancel these jobs on logout/background.
                    supervisorScope {
                        launch(Dispatchers.IO) {
                            try {
                                app.todosComponent.syncRepository().sync()
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: IOException) {
                                Log.w("TodosSync", "Network error during sync", e)
                            } catch (t: Throwable) {
                                Log.e("TodosSync", "Unexpected error during sync", t)
                            }
                        }

                        launch(Dispatchers.IO) {
                            try {
                                val result = runGameSync(
                                    applicationContext = applicationContext,
                                    auth = app.auth
                                )
                                Log.d("GameSync", "Foreground sync result: $result")
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: IOException) {
                                Log.w("GameSync", "Network error during foreground sync", e)
                            } catch (t: Throwable) {
                                Log.e("GameSync", "Unexpected foreground sync error", t)
                            }
                        }
                    }
                }
            }
        }

        val uri = intent.data
        val animalId = uri?.lastPathSegment

        setContent {
            AppNavigation(
                diamondsCountRepository = diamondsCountRepository,
                todosDaggerVmFactory = todosDaggerVmFactory,
                startDestination = if (animalId != null) {
                    "animal?id=${Uri.encode(animalId)}"
                } else {
                    "todos"
                }
            )
        }
    }
}

package pro.progr.saveanowl

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diamondsCountRepository =
            (application as SaveAnOwlApplication).todosComponent.diamondsCountRepository()
        val todosDaggerVmFactory = (application as SaveAnOwlApplication).todosComponent.daggerViewModelFactory()

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
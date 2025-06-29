package pro.progr.saveanowl

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

        setContent {
            AppNavigation(diamondsCountRepository = diamondsCountRepository,
                todosDaggerVmFactory = todosDaggerVmFactory)
        }

    }
}
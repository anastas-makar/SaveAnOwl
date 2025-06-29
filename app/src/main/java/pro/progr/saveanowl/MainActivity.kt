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

        setContent {
            AppNavigation(diamondsCountRepository = diamondsCountRepository)
        }

    }
}
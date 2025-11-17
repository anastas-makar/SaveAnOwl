package pro.progr.saveanowl

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import pro.progr.diamondapi.GetDiamondsCountInterface
import pro.progr.fallingdiamonds.composable.SundukDrawerWidget
import pro.progr.diamondtimer.TimerDrawerWidget
import pro.progr.owlgame.presentation.ui.WidgetScreen
import pro.progr.saveanowl.auth.AuthUiState
import pro.progr.saveanowl.auth.VkAuthViewModel
import pro.progr.saveanowl.auth.VkAuthViewModelFactory
import pro.progr.saveanowl.auth.VkLoginButton
import pro.progr.saveanowl.auth.VkWelcomeText

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    diamondViewModel: GetDiamondsCountInterface,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val ctx = LocalContext.current.applicationContext as SaveAnOwlApplication
                val vm: VkAuthViewModel = viewModel(factory = VkAuthViewModelFactory(ctx))
                val state by vm.ui.collectAsState()
                when (val s = state) {
                    is AuthUiState.LoggedOut -> VkLoginButton(onClick = vm::signIn, modifier = Modifier.padding(16.dp))
                    is AuthUiState.Loading   -> CircularProgressIndicator()
                    is AuthUiState.LoggedIn  -> VkWelcomeText(name = s.name.toString())
                    is AuthUiState.Error     -> Text("Ошибка: ${s.message}")
                }

                // Две колонки сверху
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Левая карточка
                    Card(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        SundukDrawerWidget(
                            diamondsTotal = diamondViewModel.getDiamondsCount()
                                .collectAsState(initial = 0),
                            { navController.navigate("sunduk") }
                        )
                    }

                    // Правая карточка с другим виджетом
                    Card(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        TimerDrawerWidget(
                            diamondsTotal = diamondViewModel.getDiamondsCount()
                                .collectAsState(initial = 0),
                            { navController.navigate("timer") }
                        )
                    }

                }

                // Остальной контент дровера
                WidgetScreen(navController)
            }
        },
        content = content
    )
}
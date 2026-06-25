package pro.progr.saveanowl.drawer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import pro.progr.authvk.AuthUiState
import pro.progr.owlgame.presentation.viewmodel.WidgetViewModel
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pro.progr.authvk.NotAuthorizedScreen
import pro.progr.authvk.VkLoginButton
import pro.progr.authvk.VkWelcomeRow
import pro.progr.diamondtimer.TimerDrawerWidget
import pro.progr.fallingdiamonds.composable.SundukDrawerWidget

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawerGridContent(
    authState: AuthUiState,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    isAuthorized: Boolean,
    diamondsTotalState: State<Int>,
    navController: NavHostController,
    viewModel: WidgetViewModel
) {
    val menuItems = viewModel.menuItems.value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            when (val s = authState) {
                is AuthUiState.LoggedOut -> {
                    VkLoginButton(
                        onClick = onLogin,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                is AuthUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is AuthUiState.LoggedIn -> {
                    VkWelcomeRow(
                        name = s.name,
                        onLogout = onLogout
                    )
                }

                is AuthUiState.Error -> {
                    Text(
                        text = "Ошибка: ${s.message}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Card(
                    modifier = Modifier.weight(1f)
                ) {
                    SundukDrawerWidget(
                        diamondsTotal = diamondsTotalState,
                        navFun = { navController.navigate("sunduk") }
                    )
                }

                Card(
                    modifier = Modifier.weight(1f)
                ) {
                    TimerDrawerWidget(
                        diamondsTotal = diamondsTotalState,
                        navFun = { navController.navigate("timer") }
                    )
                }
            }
        }

        if (isAuthorized) {
            itemsIndexed(menuItems) { _, menuItem ->
                DrawerMenuCard(
                    menuItem = menuItem,
                    onClick = {
                        navController.navigate(menuItem.navigateTo)
                    }
                )
            }
        } else {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                NotAuthorizedScreen("Войдите через VK ID, чтобы играть")
            }
        }
    }
}
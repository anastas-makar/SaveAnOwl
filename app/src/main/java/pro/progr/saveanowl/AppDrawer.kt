package pro.progr.saveanowl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pro.progr.diamondapi.GetDiamondsCountInterface
import pro.progr.fallingdiamonds.composable.SundukDrawerWidget
import pro.progr.owlgame.presentation.ui.WidgetScreen

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    viewModel: GetDiamondsCountInterface,
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
                            diamondsTotal = viewModel.getDiamondsCount()
                                .collectAsState(initial = 0),
                            { navController.navigate("sunduk") }
                        )
                    }

                    // Правая карточка с другим виджетом
                    Card(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        SundukDrawerWidget(
                            diamondsTotal = viewModel.getDiamondsCount()
                                .collectAsState(initial = 0),
                            { navController.navigate("sunduk") }
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
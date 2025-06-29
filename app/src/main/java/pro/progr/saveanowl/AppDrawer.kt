package pro.progr.saveanowl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pro.progr.diamondapi.GetDiamondsCountInterface
import pro.progr.fallingdiamonds.composable.SundukDrawerWidget
import pro.progr.owlgame.presentation.ui.WidgetScreen

@Composable
fun AppDrawer(drawerState: DrawerState,
              viewModel: GetDiamondsCountInterface,
              navController: NavHostController,
              content : @Composable () -> Unit) {

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)) {
                SundukDrawerWidget(
                    diamondsTotal = viewModel.getDiamondsCount().collectAsState(initial = 0),
                    {navController.navigate("sunduk")})
                WidgetScreen(navController)
            }

        },
        content = content
    )
}
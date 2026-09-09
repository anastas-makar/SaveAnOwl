package pro.progr.saveanowl.drawer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import pro.progr.authvk.VkAuthViewModel
import pro.progr.authvk.VkAuthViewModelFactory
import pro.progr.diamondapi.GetDiamondsCountInterface
import pro.progr.owlgame.presentation.viewmodel.WidgetViewModel
import pro.progr.owlgame.presentation.viewmodel.dagger.DaggerWidgetViewModel
import pro.progr.saveanowl.SaveAnOwlApplication

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppDrawer(
    drawerState: DrawerState,
    diamondViewModel: GetDiamondsCountInterface,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val app = LocalContext.current.applicationContext as SaveAnOwlApplication

    val isAuthorized by app.auth.isAuthorized().collectAsState(false)
    val diamondsTotalState = diamondViewModel.getDiamondsCount()
        .collectAsState(initial = 0)

    val vm: VkAuthViewModel = viewModel(
        factory = VkAuthViewModelFactory(
            auth = app.auth,
            personalCrypto = app.personalCrypto,
            api = app.authApi
        )
    )
    val authState by vm.ui.collectAsState()

    val widgetViewModel = DaggerWidgetViewModel<WidgetViewModel>(app.owlGameComponent)

    LaunchedEffect(widgetViewModel) {
        widgetViewModel.updateMenuList()
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (widgetViewModel.isLoading.value) {
                CircularProgressIndicator()
            } else DrawerGridContent(
                authState = authState,
                onLogin = vm::signIn,
                onLogout = vm::logout,
                isAuthorized = isAuthorized,
                diamondsTotalState = diamondsTotalState,
                navController = navController,
                viewModel = widgetViewModel
            )
        },
        content = content
    )
}

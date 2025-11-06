package pro.progr.saveanowl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pro.progr.diamondapi.AuthInterface
import pro.progr.diamondtimer.TimerScreen
import pro.progr.diamondtimer.TimerViewModel
import pro.progr.diamondtimer.TimerViewModelFactory
import pro.progr.fallingdiamonds.composable.SundukScreen
import pro.progr.owlgame.presentation.navigation.OwlNavigation
import pro.progr.todos.DiamondViewModel
import pro.progr.todos.DiamondsCountRepository
import pro.progr.todos.TodosNavigation
import pro.progr.todos.dagger2.DaggerViewModelFactory

@kotlinx.coroutines.ExperimentalCoroutinesApi
@Composable
fun AppNavigation(diamondsCountRepository: DiamondsCountRepository,
                  todosDaggerVmFactory: DaggerViewModelFactory,
                  auth: AuthInterface,
                  startDestination: String = "todos"
) {
    val navController = rememberNavController()
    val diamondViewModel : DiamondViewModel = viewModel(factory = todosDaggerVmFactory)

    NavHost(navController = navController, startDestination = startDestination) {
        composable("todos") {
            TodosNavigation(
                appDrawer = { a, b, c, d -> AppDrawer(a, b, c, d) },
                externalNavController = navController,
                diamondViewModel = diamondViewModel,
                auth = auth
            )
        }

        //Навигация в модуле "Falling Diamonds"
        composable("sunduk") { backStackEntry ->
            SundukScreen(
                { navController.popBackStack() },
                diamondViewModel)
        }

        //Навигация в модуле "Diamond Timer"
        composable("timer") { backStackEntry ->
            val vm: TimerViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = TimerViewModelFactory(diamondViewModel, minutes = 25, reward = 10)
            )

            TimerScreen(
                state = vm.state,
                diamondsCount = vm.diamondsCount,
                onStart = vm::start,
                onPause = vm::pause,
                onResume = vm::resume,
                onReset = vm::reset,
                onRestart = vm::restart,
                onClaim = vm::claim,
                onClaimAndRestart = vm::claimAndRestart,
                onChangeDurationMinutes = vm::setDurationMinutes,
                onChangeReward = vm::setReward
            )
        }

        //Навигация в модуле "Спаси сову"
        composable("owl_navigation") {
            OwlNavigation (diamondDao = diamondsCountRepository,
                backToMain = { navController.popBackStack()})
        }

        //Навигация в модуле "Спаси сову"
        composable("owl_navigation/pouch") {
            OwlNavigation(startDestination = "pouch", diamondDao = diamondsCountRepository,
                backToMain = { navController.popBackStack() })
        }

        //Навигация в модуле "Спаси сову"
        composable(
            route = "animal?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")

            OwlNavigation(startDestination = "animal_searching/$id", diamondDao = diamondsCountRepository,
                backToMain = { navController.navigate("todos") })

        }

    }
}
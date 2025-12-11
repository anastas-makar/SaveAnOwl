package pro.progr.saveanowl

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                  startDestination: String = "todos"
) {
    val navController = rememberNavController()
    val diamondViewModel : DiamondViewModel = viewModel(factory = todosDaggerVmFactory)

    val app = LocalContext.current.applicationContext as SaveAnOwlApplication

    NavHost(navController = navController, startDestination = startDestination) {
        composable("todos") {
            TodosNavigation(
                appDrawer = { a, b, c, d -> AppDrawer(a, b, c, d) },
                externalNavController = navController,
                diamondViewModel = diamondViewModel,
                component = app.todosComponent
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
                backFun = {navController.popBackStack()},
                vm = vm
            )
        }

        //Навигация в модуле "Спаси сову"
        composable("owl_navigation") {
            OwlNavigation (diamondDao = diamondsCountRepository,
                backToMain = { navController.popBackStack()},
                component = app.owlGameComponent)
        }

        //Навигация в модуле "Спаси сову"
        composable("owl_navigation/pouch") {
            OwlNavigation(startDestination = "pouch", diamondDao = diamondsCountRepository,
                backToMain = { navController.popBackStack() },
                component = app.owlGameComponent)
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
                backToMain = { navController.navigate("todos") },
                component = app.owlGameComponent)

        }

    }
}
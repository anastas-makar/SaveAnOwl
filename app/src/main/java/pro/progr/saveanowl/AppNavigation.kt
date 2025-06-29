package pro.progr.saveanowl

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pro.progr.owlgame.presentation.navigation.OwlNavigation
import pro.progr.todos.DiamondsCountRepository
import pro.progr.todos.TodosNavigation

@OptIn(ExperimentalMaterialApi::class)
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Composable
fun AppNavigation(diamondsCountRepository: DiamondsCountRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "todos") {
        composable("todos") {
            TodosNavigation(
                appDrawer = { a, b, c, d -> AppDrawer(a, b, c, d) },
                externalNavController = navController
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
                backToMain = { navController.navigate("calendar") })

        }

    }
}
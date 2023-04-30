package space.jay.wirebarley.ui.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import space.jay.wirebarley.feature.exchangeRateCalculation.routeExchangeRateCalculation
import space.jay.wirebarley.feature.exchangeRateCalculation.toExchangeRateCalculation

@Composable
fun NavHostMain(
    modifier : Modifier = Modifier,
    navController : NavHostController,
    screenStart : String = routeExchangeRateCalculation,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = screenStart
    ) {
        toExchangeRateCalculation()
    }
}
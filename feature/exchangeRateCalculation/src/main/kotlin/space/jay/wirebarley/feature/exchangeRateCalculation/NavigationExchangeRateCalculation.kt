package space.jay.wirebarley.feature.exchangeRateCalculation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val routeExchangeRateCalculation = "routeExchangeRateCalculation"

fun NavController.navigateToExchangeRateCalculation(navOptions : NavOptions? = null) {
    this.navigate(routeExchangeRateCalculation, navOptions)
}

fun NavGraphBuilder.toExchangeRateCalculation() {
    composable(
        route = routeExchangeRateCalculation
    ) {
        val viewModel : ViewModelExchangeRateCalculation = hiltViewModel()
        val stateUI by viewModel.stateUIExchangedRateCalculation.collectAsState()
        ScreenExchangeRateCalculation(
            stateUI = stateUI,
            onChangedExchangeAmount = viewModel::setExchangeAmount,
            onChangedCountry = viewModel::selectCountry,
            onGetCurrency = viewModel::getCurrency
        )
    }
}
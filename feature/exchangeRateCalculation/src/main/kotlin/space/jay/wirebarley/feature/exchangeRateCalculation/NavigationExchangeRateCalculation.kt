package space.jay.wirebarley.feature.exchangeRateCalculation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

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
package space.jay.wirebarley

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import space.jay.wirebarley.feature.exchangeRateCalculation.ScreenExchangeRateCalculation
import space.jay.wirebarley.feature.exchangeRateCalculation.ViewModelExchangeRateCalculation
import space.jay.wirebarley.ui.theme.WirebarleyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WirebarleyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
        }
    }
}

@Composable
fun Greeting(name : String, modifier : Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WirebarleyTheme {
        Greeting("Android")
    }
}
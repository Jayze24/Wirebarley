package space.jay.wirebarley.feature.exchangeRateCalculation

import android.widget.NumberPicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import space.jay.wirebarley.core.common.wapper.ErrorMessage
import space.jay.wirebarley.core.model.TypeCountryAndQuote

@Composable
fun ScreenExchangeRateCalculation(
    modifier : Modifier = Modifier,
    stateUI : StateUIExchangedRateCalculation,
    onChangedExchangeAmount : (String) -> Unit,
    onChangedCountry : (TypeCountryAndQuote) -> Unit
) {

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.exchange_rate_calculation),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextContentDetail(title = R.string.remittance_country, content = stateUI.fromSelectedCountry.quote)
        Spacer(modifier = Modifier.height(24.dp))
        TextContentDetail(title = R.string.recipient_country, content = stateUI.toSelectedCountry.quote)
        Spacer(modifier = Modifier.height(24.dp))
        TextContentDetail(title = R.string.exchange_rate, content = stateUI.exchangeRate)
        Spacer(modifier = Modifier.height(24.dp))
        TextContentDetail(title = R.string.check_time, content = stateUI.checkTime)

        if (stateUI is StateUIExchangedRateCalculation.HasCurrency) {
            Spacer(modifier = Modifier.height(24.dp))
            TextContentRemittanceAmount(
                exchangeAmount = stateUI.exchangeAmount,
                onChangedExchangeAmount = onChangedExchangeAmount,
                quote = stateUI.fromSelectedCountry.quote,
                errorMessage = stateUI.errorMessageExchangedAmount
            )
            Text(text = stateUI.receivedAmount)
        }

        Spacer(modifier = Modifier.weight(1f))
        PickerCountry(stateUI = stateUI, onChangedCountry = onChangedCountry)
    }
}

@Composable
fun TextContentDetail(title : Int, content : String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextContentTitle(title = title)
        Text(
            modifier = Modifier.weight(3f),
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextContentRemittanceAmount(
    exchangeAmount : String,
    onChangedExchangeAmount : (String) -> Unit,
    quote : String,
    errorMessage : ErrorMessage?
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextContentTitle(title = R.string.remittance_amount)
        Column(modifier = Modifier.weight(3f)) {
            Row {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = exchangeAmount,
                    onValueChange = onChangedExchangeAmount,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    )
                )
                Text(
                    modifier = Modifier.weight(2f),
                    text = quote,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (errorMessage != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun RowScope.TextContentTitle(title : Int) {
    Text(
        modifier = Modifier
            .defaultMinSize(120.dp)
            .weight(1f),
        text = "${stringResource(id = title)} : ",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.End
    )
}

@Stable
@Composable
fun PickerCountry(
    stateUI : StateUIExchangedRateCalculation,
    onChangedCountry : (TypeCountryAndQuote) -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = stateUI.listAvailableCountry.lastIndex
                displayedValues = stateUI.listAvailableCountry.map { it.quote }.toTypedArray()
                setOnValueChangedListener { _, oldVal, newVal ->
                    if (oldVal != newVal) onChangedCountry(stateUI.listAvailableCountry[newVal])
                }
            }
        }
    )
}
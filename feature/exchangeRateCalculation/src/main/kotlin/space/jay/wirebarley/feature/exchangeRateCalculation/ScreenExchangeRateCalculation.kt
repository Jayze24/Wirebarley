package space.jay.wirebarley.feature.exchangeRateCalculation

import android.widget.NumberPicker
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import space.jay.wirebarley.core.common.visualTransformation.VisualTransformationPrice
import space.jay.wirebarley.core.common.wapper.ErrorMessage
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import space.jay.wirebarley.core.model.toFullMark

@Composable
fun ScreenExchangeRateCalculation(
    modifier : Modifier = Modifier,
    stateUI : StateUIExchangedRateCalculation,
    onChangedExchangeAmount : (String) -> Unit,
    onChangedCountry : (TypeCountryAndQuote) -> Unit
) {

    Column(modifier = modifier) {
        // 타이틀
        Text(
            modifier = Modifier
                .padding(top = 32.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.exchange_rate_calculation),
            style = MaterialTheme.typography.titleLarge
        )
        // 송금 국가
        TextContentDetail(title = R.string.remittance_country, content = stateUI.fromSelectedCountry.quote)
        // 수취 국가
        TextContentDetail(title = R.string.recipient_country, content = stateUI.toSelectedCountry.quote)
        // 환율
        TextContentDetail(title = R.string.exchange_rate, content = stateUI.exchangeRate)
        //조회 시간
        TextContentDetail(title = R.string.check_time, content = stateUI.checkTime)

        if (stateUI is StateUIExchangedRateCalculation.HasCurrency) {
            // 송금액
            TextContentRemittanceAmount(
                exchangeAmount = stateUI.exchangeAmount,
                onChangedExchangeAmount = onChangedExchangeAmount,
                quote = stateUI.fromSelectedCountry.quote,
                errorMessage = stateUI.errorMessageExchangedAmount
            )

            // 수취 금액
            Text(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .fillMaxWidth(),
                text = stateUI.receivedAmount,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        PickerCountry(stateUI = stateUI, onChangedCountry = onChangedCountry)
    }
}

@Composable
fun TextContentDetail(title : Int, content : String) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        TextContentTitle(title = title)
        Text(
            modifier = Modifier.weight(3f),
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TextContentRemittanceAmount(
    exchangeAmount : String,
    onChangedExchangeAmount : (String) -> Unit,
    quote : String,
    errorMessage : ErrorMessage?
) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        TextContentTitle(title = R.string.remittance_amount)
        Column(modifier = Modifier.weight(3f)) {
            Row {
                BasicTextField(
                    modifier = Modifier
                        .weight(1f)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                        .padding(horizontal = 2.dp),
                    value = exchangeAmount,
                    onValueChange = onChangedExchangeAmount,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = VisualTransformationPrice()
                )
                Text(
                    modifier = Modifier.weight(2f),
                    text = " $quote",
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
                displayedValues = stateUI.listAvailableCountry.map { it.toFullMark(context) }.toTypedArray()
                setOnValueChangedListener { _, oldVal, newVal ->
                    if (oldVal != newVal) onChangedCountry(stateUI.listAvailableCountry[newVal])
                }
            }
        }
    )
}
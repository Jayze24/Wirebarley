package space.jay.wirebarley.feature.exchangeRateCalculation

import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    onChangedCountry : (TypeCountryAndQuote) -> Unit,
    onGetCurrency : () -> Unit
) {
    val context = LocalContext.current
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
        TextContentDetail(title = R.string.remittance_country, content = stateUI.fromSelectedCountry.toFullMark(context))
        // 수취 국가
        TextContentDetail(title = R.string.recipient_country, content = stateUI.toSelectedCountry.toFullMark(context))
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
            TextReceivedAmount(stateUI.receivedAmount)
        }
        // 갱신 버튼
        ButtonRefreshCurrency(onGetCurrency = onGetCurrency)

        Spacer(modifier = Modifier.weight(1f))
        // 국가 선택
        PickerCountry(stateUI = stateUI, onChangedCountry = onChangedCountry)

        // 통신 실패시 토스트 띄우기
        ShowErrorMessageFromNoCurrency(stateUI = stateUI)
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
    val text = stringResource(id = title)
    Text(
        modifier = Modifier
            .defaultMinSize(120.dp)
            .weight(1f)
            .semantics { contentDescription = text },
        text = "$text : ",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.End
    )
}

@Composable
fun TextReceivedAmount(receivedAmount : String) {
    Text(
        modifier = Modifier
            .padding(top = 48.dp)
            .fillMaxWidth(),
        text = receivedAmount,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center
    )
}

@Stable
@Composable
fun ColumnScope.ButtonRefreshCurrency(
    onGetCurrency : () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .wrapContentSize()
            .align(CenterHorizontally),
        onClick = onGetCurrency
    ) {
        Text(text = stringResource(id = R.string.get_latest_exchange_rates))
    }
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

@Composable
fun ShowErrorMessageFromNoCurrency(stateUI : StateUIExchangedRateCalculation) {
    if (stateUI is StateUIExchangedRateCalculation.NoCurrency && stateUI.errorMessageCurrency != null) {
        val context = LocalContext.current
        LaunchedEffect(key1 = stateUI.errorMessageCurrency.id) {
            Toast.makeText(context, stateUI.errorMessageCurrency.message, Toast.LENGTH_LONG).show()
        }
    }
}
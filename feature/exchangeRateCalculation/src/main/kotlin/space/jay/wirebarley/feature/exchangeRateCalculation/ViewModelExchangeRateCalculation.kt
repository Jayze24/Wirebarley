package space.jay.wirebarley.feature.exchangeRateCalculation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import space.jay.wirebarley.core.common.wapper.ErrorMessage
import space.jay.wirebarley.core.common.wapper.Fail
import space.jay.wirebarley.core.common.wapper.Success
import space.jay.wirebarley.core.domain.UseCaseGetCurrency
import space.jay.wirebarley.core.domain.UseCaseGetExchangeRateCalculation
import space.jay.wirebarley.core.model.TypeCountryAndQuote
import javax.inject.Inject

@HiltViewModel
class ViewModelExchangeRateCalculation @Inject constructor(
    private val application : Application,
    private val useCaseGetCurrency : UseCaseGetCurrency,
    private val useCaseGetExchangeRateCalculation : UseCaseGetExchangeRateCalculation
) : ViewModel() {

    private val stateViewModelExchangedRateCalculation = MutableStateFlow(StateViewModelExchangedRateCalculation(isLoadingCurrency = true))
    val stateUIExchangedRateCalculation : StateFlow<StateUIExchangedRateCalculation> = stateViewModelExchangedRateCalculation
        .map { it.toStateUi(application) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            stateViewModelExchangedRateCalculation.value.toStateUi(application)
        )

    init {
        getCurrency()
    }

    fun getCurrency() {
        stateViewModelExchangedRateCalculation.update { it.copy(isLoadingCurrency = true) }
        viewModelScope.launch {
            val result = useCaseGetCurrency(
                from = stateViewModelExchangedRateCalculation.value.fromSelectedCountry,
                to = stateViewModelExchangedRateCalculation.value.listAvailableCountry
            )
            stateViewModelExchangedRateCalculation.update { state ->
                when (result) {
                    is Success -> state.copy(isLoadingCurrency = false, currency = state.getNewCurrency(result.data))
                    is Fail -> state.copy(
                        isLoadingCurrency = false,
                        currency = null,
                        errorMessageCurrency = result.message?.let { ErrorMessage(message = it) }
                    )
                }
            }
            if (result is Success) requestExchangedAmount()
        }
    }

    fun setExchangeAmount(amount : String) {
        val amountNumber = if (amount.isEmpty()) "" else amount.filter { it.isDigit() }.take(5).toInt().toString()
        stateViewModelExchangedRateCalculation.update { it.copy(exchangeAmount = amountNumber) }
        requestExchangedAmount()
    }

    private fun requestExchangedAmount() {
        stateViewModelExchangedRateCalculation.update { state ->
            when (val result = useCaseGetExchangeRateCalculation(state.exchangeAmount, state.currency?.quotes?.get(state.toSelectedCountry))) {
                is Success -> state.copy(exchangedAmount = result.data, errorMessageExchangedAmount = null)
                is Fail -> state.copy(
                    exchangedAmount = null,
                    errorMessageExchangedAmount = result.message?.let { ErrorMessage(message = it) }
                )
            }
        }
    }

    fun selectCountry(type : TypeCountryAndQuote) {
        stateViewModelExchangedRateCalculation.update { it.copy(toSelectedCountry = type) }
        requestExchangedAmount()
    }
}
import android.content.SharedPreferences
import com.mc.currencyconvertor.CurrencyConvertorViewModel
import com.mc.model.currency_convertor.ExchangeRates
import com.mc.testing.repository.TestCurrencyRepository
import com.mc.testing.worker.TestWorkManagerSyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CurrencyViewModelTest {

    lateinit var viewModel: CurrencyConvertorViewModel
    private lateinit var currencyRepository: TestCurrencyRepository

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    private var workManagerSyncManager = TestWorkManagerSyncManager()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        currencyRepository = TestCurrencyRepository()

        MockitoAnnotations.openMocks(this)

        Dispatchers.setMain(testDispatcher)

        viewModel = CurrencyConvertorViewModel(
            currencyRepository = currencyRepository,
            sharedPreferences = sharedPreferences,
            workManagerSyncManager = workManagerSyncManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() {
        assert(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `state is not loading when syncing is done`() = testScope.runTest {
        workManagerSyncManager.emit(false)

        val job = launch {
            viewModel.uiState.take(1).toList()
        }
        job.join()
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `state has changed after a successful emission from the repository`() = testScope.runTest {
        workManagerSyncManager.emit(true)

        currencyRepository.sendExchangeRates(
            ExchangeRates(
                baseCurrency = "",
                rates = mapOf("USD" to 10.0, "EUR" to 0.92),
                lastUpdatedDate = "06-07-2024"
            )
        )

        workManagerSyncManager.emit(false)

        val job = launch {
            viewModel.uiState.take(1).toList()
        }
        job.join()

        assert(!viewModel.uiState.value.isLoading)
        assert(viewModel.uiState.value.indicativeExchangeRate.isNotEmpty())
    }
}











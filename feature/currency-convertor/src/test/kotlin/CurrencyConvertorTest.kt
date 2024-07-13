import com.mc.currencyconvertor.CurrencyConvertor
import org.junit.Test

class CurrencyConvertorTest {

    @Test
    fun `convert value in USD to EUR`() {
        val usdVsUsdPrice = 1.0
        val eurVsUsdPrice = 0.92
        val result = CurrencyConvertor.convert(
            usdVsUsdPrice,
            eurVsUsdPrice,
            1.0
        )
        assert(result == 0.92)
    }

    @Test
    fun `convert value USD to USD`() {
        val usdVsUsdPrice = 1.0
        val result = CurrencyConvertor.convert(
            usdVsUsdPrice,
            usdVsUsdPrice,
            20.5
        )
        assert(result == 20.5)
    }


    @Test
    fun `convert zero amount`() {
        val result = CurrencyConvertor.convert(
            1.0,
            1.0,
            0.0
        )

        assert(result == 0.0)
    }
}
package backend.graabackend.model.mapper

import backend.graabackend.model.response.NftResponse
import org.springframework.stereotype.Component

const val USDT_DECIMALS = 1000000
const val DUCK_COIN_DECIMALS = 1000000000

@Component
class NftMapper {
    fun asSaleMetadataHelperResponse(nftPrice: Long?): NftResponse.SaleMetadataHelperResponse {
        var price: Long = nftPrice ?: -1

        return NftResponse.SaleMetadataHelperResponse(
            onSale = when {
                nftPrice != null && nftPrice != -1L -> true
                else -> false },
            saleDuckCoinPrice = price / DUCK_COIN_DECIMALS,
            saleUsdtPrice = price / USDT_DECIMALS
        )
    }
}
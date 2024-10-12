package backend.graabackend.model.mapper

import backend.graabackend.model.response.NftResponse
import org.springframework.stereotype.Component

const val USDT_DEL = 1000

@Component
class NftMapper {
    fun asSaleMetadataHelperResponse(nftPrice: Long?): NftResponse.SaleMetadataHelperResponse {
        val price: Long = nftPrice ?: -1

        return NftResponse.SaleMetadataHelperResponse(
            onSale = when {
                nftPrice != null && nftPrice != -1L -> true
                else -> false },
            saleDuckCoinPrice = price,
            saleUsdtPrice = price / USDT_DEL
        )
    }
}
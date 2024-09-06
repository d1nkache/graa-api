package backend.graabackend.model.mapper

import backend.graabackend.model.response.NftResponse
import org.springframework.stereotype.Component

@Component
class NftMapper {
    fun asSaleMetadataHelperResponse(nftPrice: Long?): NftResponse.SaleMetadataHelperResponse {
        return NftResponse.SaleMetadataHelperResponse(
            onSale = when {
                nftPrice != null && nftPrice != -1L -> true
                else -> false },
            salePrice = nftPrice
        )
    }
}
package backend.graabackend.model.response

import org.springframework.http.HttpStatus

sealed class CollectionResponse {
    class GetCollectionFinalResponse(
        val collectionMetadata: CollectionMetadataHelperResponse,
        val nft_items: List<NftMetadataHelperResponse>
    ): CollectionResponse()

    class NftItemsHelperResponse(val nft_items: List<NftMetadataHelperResponse>): CollectionResponse()
    class NftMetadataHelperResponse(val metadata: SearchResponse.MetadataResponse): CollectionResponse()
    class CollectionOwnerHelperResponse(val address: String)

    class CollectionMetadataHelperResponse(
        val address: String,
        val metadata: SearchResponse.MetadataResponse,
//        val floorPrice: Double,
        val owner: CollectionOwnerHelperResponse,
    ): CollectionResponse()

    class AbstractCollectionErrorMessage(
        val message: String,
        val status: HttpStatus = HttpStatus.BAD_REQUEST
    ) : CollectionResponse()
}


package backend.graabackend.model.response

import org.springframework.http.HttpStatus

// придумать чтр-то классом
// нет properties и  иконки owner

sealed class NftResponse {
    class GetNftFinalResponse(
        val collectionMetadata: CollectionResponse.CollectionMetadataHelperResponse,
        val nftMetadata: NftMetadataHelperResponse
    ): NftResponse()

    class CollectionAddressHelperResponse(val address: String): NftResponse()

    class NftMetadataHelperResponse(
        val collection: CollectionAddressHelperResponse,
        val metadata: SearchResponse.MetadataResponse
    ): NftResponse()

    class AbstractNftErrorMessage(
        val message: String,
        val status: HttpStatus = HttpStatus.BAD_REQUEST
    ): NftResponse()
}
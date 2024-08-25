package backend.graabackend.model.response

import org.springframework.http.HttpStatus

// придумать чтр-то классом
// нет properties и  иконки owner

sealed class NftResponse {
    class GetNftSmartContractInfoFinalResponse(
        val success: Boolean,
        val stack: List<StackItemHelperResponse>,
        val decoded: DecodedHelperResponse
    ): NftResponse()

    class StackItemHelperResponse(
        val type: String,
        val num: String
    ): NftResponse()

    class GetNftFinalResponse(
        val collectionMetadata: CollectionResponse.CollectionMetadataHelperResponse,
        val nftMetadata: NftMetadataHelperResponse,
        val nftAttributes: List<Attribute>
    ): NftResponse()

    class CollectionAddressHelperResponse(val address: String): NftResponse()
    class DecodedHelperResponse(val full_price: String): NftResponse()

    class NftMetadataHelperResponse(
        val owner: NftOwnerHelperResponse,
        val collection: CollectionAddressHelperResponse,
        val metadata: SearchResponse.MetadataResponse
    ): NftResponse()

    class NftOwnerHelperResponse(
        val address: String
    )
    class AbstractNftErrorMessage(
        val message: String,
        val status: HttpStatus = HttpStatus.BAD_REQUEST
    ): NftResponse()

    class GraphqlFinalResponse(
        val data: AlphaNftDataHelperResponse
    ): NftResponse()

    class AlphaNftDataHelperResponse(
        val alphaNftItemByAddress: AlphaNftItemHelperResponse
    ): NftResponse()

    class AlphaNftItemHelperResponse(
        val attributes: List<Attribute>
    ): NftResponse()

    class Attribute(
        val traitType: String,
        val value: String
    ): NftResponse()
}
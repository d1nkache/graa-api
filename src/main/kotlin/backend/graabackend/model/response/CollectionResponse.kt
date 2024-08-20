package backend.graabackend.model.response

import org.springframework.http.HttpStatus
import org.springframework.security.config.http.SessionCreationPolicy
import java.time.LocalDateTime


// добавить иконку коллекции

sealed class CollectionResponse {
    class CollectionEntityFinalResponse(
        val collectionName: String,
        val collectionAddress:  String,
        val ownerAddress: String
    ): CollectionResponse()

    // внести verified в collection metadata
    class GetCollectionFinalResponse(
        val verified: Boolean,
        val collectionMetadata: CollectionMetadataHelperResponse,
        val nft_items: List<SearchResponse.NftItemResponse>
    ): CollectionResponse()

    class NftItemsHelperResponse(val nft_items: List<SearchResponse.NftItemResponse>): CollectionResponse()
    class NftMetadataHelperResponse(val metadata: SearchResponse.NftItemResponse): CollectionResponse()
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


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

    class GetCollectionFinalResponse(
        val graaVerified: Boolean,
        val collectionMetadata: CollectionMetadataHelperResponse,
        val nftItems: List<SearchResponse.NftItemResponse>
    ): CollectionResponse()

    class NftItemsHelperResponse(val nft_items: List<SearchResponse.NftItemResponse>): CollectionResponse()
    class CollectionOwnerHelperResponse(val address: String)

    class CollectionMetadataHelperResponse(
        val address: String,
        val metadata: SearchResponse.MetadataResponse,
//        val floorPrice: Double,
        val owner: CollectionOwnerHelperResponse,
        val approved_by: List<String>
    ): CollectionResponse()

    class AbstractCollectionErrorMessage(
        val message: String,
        val status: HttpStatus = HttpStatus.BAD_REQUEST
    ) : CollectionResponse()
}


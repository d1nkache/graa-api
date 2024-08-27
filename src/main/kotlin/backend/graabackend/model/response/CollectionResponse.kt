package backend.graabackend.model.response

import org.springframework.http.HttpStatus
import org.springframework.security.config.http.SessionCreationPolicy
import java.time.LocalDateTime


// добавить иконку коллекции

sealed class CollectionResponse {
    class CollectionEntityFinalResponse(
        val collectionName: String?,
        val collectionAddress:  String,
        val ownerAddress: String
    ): CollectionResponse()

    class GetCollectionFinalResponse(
        val graaVerified: Boolean,
        val floorPrice: Long,
        val collectionMetadata: CollectionMetadataHelperResponse,
        val countOfNftsInCollection: Int,
        val nftItems: List<NftItemHelperResponse>?
    ): CollectionResponse()

    class SortedNftItemsFinalResponse(val nftItems: List<NftItemHelperResponse>?): CollectionResponse()
    class NftItemsHelperResponse(val nft_items: List<SearchResponse.NftItemResponse>): CollectionResponse()
    class CollectionOwnerHelperResponse(val address: String): CollectionResponse()

    class CollectionMetadataHelperResponse(
        val address: String,
        val metadata: SearchResponse.MetadataResponse,
        val owner: CollectionOwnerHelperResponse,
        val approved_by: List<String>
    ): CollectionResponse()

    class NftItemHelperResponse(
        val name: String,
        val description: String,
        val image: String,
        val price: Long?
    ): CollectionResponse()

    class AbstractCollectionErrorMessage(
        val message: String,
        val status: HttpStatus = HttpStatus.BAD_REQUEST
    ) : CollectionResponse()
}


package backend.graabackend.model.response

import backend.graabackend.database.entities.Nfts
import org.springframework.http.HttpStatus

sealed class UserResponse{
    class UserNftItemFinalResponse(
        val nftAddress: String,
        val nftName: String,
        val ownerAddress: String,
        val collectionAddress: String
    ): UserResponse()

    class NewOrUpdatedUserNftsFinalResponse(val newNfts: List<Nfts>): UserResponse()
    class NftItemsByOwnerFinalResponse(val data: NftItemsHelperResponse): UserResponse()
    class NftItemsHelperResponse(val nftItemsByOwner: ItemsHelperResponse): UserResponse()
    class ItemsHelperResponse(val items: List<ItemDataHelperResponse>): UserResponse()

    class ItemDataHelperResponse(
        val address: String,
        val collection: CollectionResponse.CollectionOwnerHelperResponse,
        val name: String?,
        val description: String?,
        val content: ContentDataHelperResponse
    ): UserResponse()

    class ContentDataHelperResponse(
        val image: ImageUrlHelperResponse?,
    ): UserResponse()

    class ImageUrlHelperResponse(val baseUrl: String): UserResponse()

    class NftItemHelperResponse(
        val address: String,
        val metadata: NftMetadataHelperResponse,
        val owner: OwnerInfoHelperResponse,
        val collection: CollectionMetadataHelperResponse
    ): UserResponse()

    class GetUserNftsHelperResponse(val nft_items: List<NftItemHelperResponse>): UserResponse()
    class NftMetadataHelperResponse(val name: String?, val description: String?, val image: String): UserResponse()
    class OwnerInfoHelperResponse(val address: String): UserResponse()
    class CollectionMetadataHelperResponse(val address: String?): UserResponse()
    class AbstractUserErrorMessage(val message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST): UserResponse()
}
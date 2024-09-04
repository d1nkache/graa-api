package backend.graabackend.model.response

import backend.graabackend.database.entities.Nfts
import org.apache.catalina.realm.UserDatabaseRealm
import org.springframework.http.HttpStatus

sealed class UserResponse{
    class UserNftItemFinalResponse(
        val nftAddress: String,
        val nftName: String,
        val ownerAddress: String,
        val collectionAddress: String
    ): UserResponse()
    class NewOrUpdatedUserNftsFinalResponse(val newNfts: List<Nfts>): UserResponse()

    class NftItemHelperResponse(
        val address: String,
        val metadata: NftMetadataHelperResponse,
        val owner: OwnerInfoHelperResponse,
        val collection: CollectionMetadataHelperResponse?
    ): UserResponse()
    class GetUserNftsHelperResponse(val nft_items: List<NftItemHelperResponse>): UserResponse()
    class NftMetadataHelperResponse(val name: String?, val description: String?, val image: String): UserResponse()
    class OwnerInfoHelperResponse(val address: String): UserResponse()
    class CollectionMetadataHelperResponse(val address: String): UserResponse()
    class AbstractUserErrorMessage(val message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST): UserResponse()
}
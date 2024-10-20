package backend.graabackend.model.mapper

import backend.graabackend.controller.helpers.NftControllerHelper
import backend.graabackend.model.response.UserResponse
import backend.graabackend.database.entities.Nfts

import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val nftHelper: NftControllerHelper
) {
    suspend fun asNftsEntity(nftItem: UserResponse.ItemDataHelperResponse, walletAddress: String): Nfts {
        val collectionAddress = nftItem.collection.address ?: ""
        val nftItemName = nftItem.name ?: ""
        val nftDescription = nftItem.description ?: ""
        val image = nftItem.content.image?.baseUrl ?: ""
        println(walletAddress)

        return Nfts(
            nftAddress = nftHelper.changeNftAddressFormat(nftItem.address),
            nftName = nftItemName.lowercase(),
            nftOwnerAddress = walletAddress,
            collectionAddress = collectionAddress,
            nftDescription = nftDescription,
            nftImage = image,
            nftTonPrice = -1
        )
    }
}
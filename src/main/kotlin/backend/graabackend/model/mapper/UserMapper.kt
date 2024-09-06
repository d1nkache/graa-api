package backend.graabackend.model.mapper

import backend.graabackend.model.response.UserResponse
import backend.graabackend.database.entities.Nfts

import org.springframework.stereotype.Component

@Component
class UserMapper {
    suspend fun asNftsEntity(nftItem: UserResponse.ItemDataHelperResponse, walletAddress: String): Nfts {
        val collectionAddress = nftItem.collection.address ?: ""
        val nftItemName = nftItem.name ?: ""
        val nftDescription = nftItem.description ?: ""
        val image = nftItem.content.image?.baseUrl ?: ""

        return Nfts(
            nftAddress = nftItem.address,
            nftName = nftItemName,
            nftOwnerAddress = walletAddress,
            collectionAddress = collectionAddress,
            nftDescription = nftDescription,
            nftImage = image,
            nftTonPrice = -1
        )
    }
}
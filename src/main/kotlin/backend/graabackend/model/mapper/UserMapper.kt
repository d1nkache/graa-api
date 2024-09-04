package backend.graabackend.model.mapper

import backend.graabackend.model.response.UserResponse
import backend.graabackend.database.entities.Nfts

import org.springframework.stereotype.Component

@Component
class UserMapper {
    suspend fun asNftsEntity(nftItem: UserResponse.NftItemHelperResponse): Nfts {
        val collectionAddress = nftItem.collection?.address ?: ""
        val nftItemName = nftItem.metadata.name ?: ""
        val nftDescription = nftItem.metadata.description ?: ""

        return Nfts(
            nftAddress = nftItem.address,
            nftName = nftItemName,
            nftOwnerAddress = nftItem.owner.address,
            collectionAddress = collectionAddress,
            nftDescription = nftDescription,
            nftImage = nftItem.metadata.image,
            nftTonPrice = -1
        )
    }
}
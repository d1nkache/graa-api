package backend.graabackend.model.mapper

import backend.graabackend.database.entities.VerifiedCollections
import backend.graabackend.database.dao.VerifiedCollectionsDao
import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.database.dao.NftsDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.springframework.stereotype.Component

import org.ton.block.AddrStd

@Component
class CollectionMapper(
    private val verifiedCollectionsDao: VerifiedCollectionsDao,
    private val nftsDao: NftsDao
) {
    suspend fun asEmptyNftGetCollectionFinalResponse(collectionMetadata: CollectionResponse.CollectionMetadataHelperResponse): CollectionResponse.GetCollectionFinalResponse {
        val verifiedCollection = withContext(Dispatchers.IO) {
            verifiedCollectionsDao.findVerifiedCollectionByCollectionAddress(collectionMetadata.address)
        }
        val graaVerified = when(verifiedCollection) {
            null -> false
            else -> true
        }

        return CollectionResponse.GetCollectionFinalResponse(
            graaVerified = graaVerified,
            floorPrice = -1,
            collectionMetadata = CollectionResponse.CollectionMetadataHelperResponse(
                address = collectionMetadata.address,
                metadata = collectionMetadata.metadata,
                owner = collectionMetadata.owner,
                approved_by = collectionMetadata.approved_by
            ),
            countOfNftsInCollection = 0,
            nftItems = null
        )
    }

    suspend fun asNftItemHelperResponse(nft: SearchResponse.NftItemResponse): CollectionResponse.NftItemHelperResponse {
        val currentNftInDatabase = withContext(Dispatchers.IO) {
            nftsDao.findEntityByNftAddress(nft.address)
        }
        nft.address = AddrStd.toString(AddrStd(address = nft.address))
        val currentNftPrice: Long = currentNftInDatabase?.nftTonPrice ?: -1L
        val nftName = nft.metadata.name ?: ""
        val nftDescription = nft.metadata.description ?: ""

        return CollectionResponse.NftItemHelperResponse(
            name = nftName,
            address = nft.address,
            description = nftDescription,
            image = nft.metadata.image,
            price = currentNftPrice
        )
    }

    suspend fun asGetCollectionFinalResponse(
        collectionMetadata: CollectionResponse.CollectionMetadataHelperResponse,
        listOfCollectionNfts: CollectionResponse.NftItemsHelperResponse,
        listOfCollectionNftsWithPrice: List<CollectionResponse.NftItemHelperResponse>
    ): CollectionResponse.GetCollectionFinalResponse {
        val verifiedCollection = withContext(Dispatchers.IO) {
            verifiedCollectionsDao.findVerifiedCollectionByCollectionAddress(collectionMetadata.address)
        }
        val graaVerified = when(verifiedCollection) {
            null -> false
            else -> true
        }
        var floorPrice: Long = Long.MAX_VALUE

        for (elem in listOfCollectionNftsWithPrice) {
            elem.address = AddrStd.toString(address = AddrStd(elem.address))

            if (elem.price != null && elem.price != -1L && elem.price < floorPrice) {
                floorPrice = elem.price
            }
        }
        if (floorPrice == Long.MAX_VALUE) floorPrice = -1

        if (collectionMetadata.owner.address != null) {
            collectionMetadata.owner.address = AddrStd.toString(
                address = AddrStd(
                    address = collectionMetadata.owner.address!!
                ),
//                bounceable = false
            )
        }


        return CollectionResponse.GetCollectionFinalResponse(
            graaVerified =  graaVerified,
            floorPrice = floorPrice,
            collectionMetadata = CollectionResponse.CollectionMetadataHelperResponse(
                address = AddrStd.toString(address = AddrStd(collectionMetadata.address)),
                metadata = collectionMetadata.metadata,
                owner = collectionMetadata.owner,
                approved_by = collectionMetadata.approved_by
            ),
            countOfNftsInCollection = listOfCollectionNfts.nft_items.size,
            nftItems = listOfCollectionNftsWithPrice
        )
    }

    suspend fun asVerifiedCollectionEntity(collectionMetadata: CollectionResponse.CollectionMetadataHelperResponse): VerifiedCollections {
        val ownerAddress: String = collectionMetadata.owner.address ?: ""

        return VerifiedCollections(
            collectionName = collectionMetadata.metadata.name,
            collectionAddress = collectionMetadata.address,
            ownerAddress = ownerAddress
        )
    }

    suspend fun asCollectionEntityFinalResponse(collectionMetadata: CollectionResponse.CollectionMetadataHelperResponse): CollectionResponse.CollectionEntityFinalResponse {
        val ownerAddress: String = collectionMetadata.owner.address ?: ""

        return CollectionResponse.CollectionEntityFinalResponse(
            collectionName = collectionMetadata.metadata.name,
            collectionAddress = collectionMetadata.address,
            ownerAddress = ownerAddress
        )
    }
}
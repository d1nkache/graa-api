package backend.graabackend.model.mapper

import backend.graabackend.database.entities.GlobalSearchCollections
import backend.graabackend.database.entities.GlobalSearchNfts
import backend.graabackend.database.entities.Nfts

import backend.graabackend.model.response.SearchResponse

import org.springframework.stereotype.Component

import org.ton.block.AddrStd

@Component
class SearchMapper {
    suspend fun asMetadataResponse(itemMetadata: SearchResponse.SearchItemResponse) : SearchResponse.MetadataResponse {
        if (itemMetadata.address == "") return this.asEmptyMetadataResponse()

        return SearchResponse.MetadataResponse(
            address = AddrStd(address = itemMetadata.address).toString(userFriendly = true),
            name = itemMetadata.metadata.name,
            image = itemMetadata.metadata.image,
            description = itemMetadata.metadata.description,
        )
    }

    suspend fun asMetadataResponseFromNftEntity(nft: Nfts) : SearchResponse.MetadataResponse {
        return SearchResponse.MetadataResponse(
            address = AddrStd(address = nft.nftAddress).toString(userFriendly = true),
            name = nft.nftName,
            image = nft.nftImage,
            description = nft.nftDescription,
        )
    }

    suspend fun asEmptyMetadataResponse() : SearchResponse.MetadataResponse {
        return SearchResponse.MetadataResponse(
            address = "",
            name = "",
            image = "",
            description = ""
        )
    }

    suspend fun asMetadataResponseFromGlobalSearchCollections(collections: List<GlobalSearchCollections>) : List<SearchResponse.MetadataResponse> {
        val resultSearch: MutableList<SearchResponse.MetadataResponse> = mutableListOf()

        for (collection in collections) {
            resultSearch.add(
                SearchResponse.MetadataResponse(
                    address = AddrStd(address = collection.collectionAddress).toString(userFriendly = true),
                    name = collection.collectionName,
                    image = collection.collectionImage,
                    description = collection.collectionDescription
                )
            )
        }

        return resultSearch
    }

    suspend fun asMetadataResponseFromGlobalSearchNfts(nfts: List<GlobalSearchNfts>, collectionAddress: String? = null) : List<SearchResponse.MetadataResponse> {
        val resultSearch: MutableList<SearchResponse.MetadataResponse> = mutableListOf()

        for (nft in nfts) {
            if (collectionAddress != null) {
                if (nft.nftCollection == collectionAddress) {
                    resultSearch.add(
                        SearchResponse.MetadataResponse(
                            address = AddrStd(address = nft.nftAddress).toString(userFriendly = true),
                            name = nft.nftName,
                            image = nft.nftImage,
                            description = nft.nftDescription
                        )
                    )
                }
            }

            resultSearch.add(
                SearchResponse.MetadataResponse(
                    address = AddrStd(address = nft.nftAddress).toString(userFriendly = true),
                    name = nft.nftName,
                    image = nft.nftImage,
                    description = nft.nftDescription
                )
            )
        }

        return resultSearch
    }

    suspend fun asSearchAccountResponse(accountMetadata: SearchResponse.SearchAccountResponse) : SearchResponse.SearchAccountResponse {
        return SearchResponse.SearchAccountResponse(
            name = accountMetadata.name,
            icon = accountMetadata.icon
        )
    }
}
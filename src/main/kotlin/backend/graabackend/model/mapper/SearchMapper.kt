package backend.graabackend.model.mapper

import backend.graabackend.database.entities.GlobalSearchCollections
import backend.graabackend.database.entities.GlobalSearchNfts
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.database.entities.Nfts
import org.springframework.stereotype.Component

@Component
class SearchMapper {
    suspend fun asMetadataResponse(itemMetadata: SearchResponse.SearchItemResponse) : SearchResponse.MetadataResponse {
        return SearchResponse.MetadataResponse(
            address = itemMetadata.metadata.address,
            name = itemMetadata.metadata.name,
            image = itemMetadata.metadata.image,
            description = itemMetadata.metadata.description,
        )
    }

    suspend fun asMetadataResponseFromNftEntity(nft: Nfts) : SearchResponse.MetadataResponse {
        return SearchResponse.MetadataResponse(
            address = nft.nftAddress,
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
                    address = collection.collectionAddress,
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
                            address = nft.nftAddress,
                            name = nft.nftName,
                            image = nft.nftImage,
                            description = nft.nftDescription
                        )
                    )
                }
            }

            resultSearch.add(
                SearchResponse.MetadataResponse(
                    address = nft.nftAddress,
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
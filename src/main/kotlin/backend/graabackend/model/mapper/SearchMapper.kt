package backend.graabackend.model.mapper

import backend.graabackend.model.response.SearchResponse
import backend.graabackend.database.entities.Nfts
import org.springframework.stereotype.Component

@Component
class SearchMapper {
    suspend fun asMetadataResponse(itemMetadata: SearchResponse.SearchItemResponse) : SearchResponse.MetadataResponse {
        return SearchResponse.MetadataResponse(
            name = itemMetadata.metadata.name,
            image = itemMetadata.metadata.image,
            description = itemMetadata.metadata.description
        )
    }

    suspend fun asMetadataResponseFromNftEntity(nft: Nfts) : SearchResponse.MetadataResponse {
        return SearchResponse.MetadataResponse(
            name = nft.nftName,
            image = nft.nftImage,
            description = nft.nftDescription
        )
    }

    suspend fun asEmptyMetadataResponse() : SearchResponse.MetadataResponse {
        return SearchResponse.MetadataResponse(
            name = "",
            image = "",
            description = ""
        )
    }

    suspend fun asSearchAccountResponse(accountMetadata: SearchResponse.SearchAccountResponse) : SearchResponse.SearchAccountResponse {
        return SearchResponse.SearchAccountResponse(
            name = accountMetadata.name,
            icon = accountMetadata.icon
        )
    }

}
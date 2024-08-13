package backend.graabackend.model.mapper

import backend.graabackend.model.response.SearchResponse
import org.springframework.stereotype.Component

@Component
class SearchMapper{
    fun asMetadataResponse(searchResponse: SearchResponse.SearchItemResponse) = SearchResponse.MetadataResponse(
        name = searchResponse.metadata.name,
        image =  searchResponse.metadata.image,
        description = searchResponse.metadata.description
    )
}

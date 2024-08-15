package backend.graabackend.model.response

import org.springframework.http.HttpStatus

sealed class CollectionResponse {
    class NftItemsResponse(val metadata: SearchResponse.MetadataResponse): CollectionResponse()
    class AllCollectionNftResponse(val nft_items: List<NftItemsResponse>): CollectionResponse()
    class AbstractCollectionErrorMessage(val message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST) : CollectionResponse()

}

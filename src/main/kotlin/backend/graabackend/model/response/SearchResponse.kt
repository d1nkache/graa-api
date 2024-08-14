package backend.graabackend.model.response

import backend.graabackend.model.message.errors.AbstractErrorMessage
import org.springframework.http.HttpStatus

sealed class SearchResponse() {
    class MetadataResponse(val name: String, val image: String, val description: String): SearchResponse()
    class SearchItemResponse(val metadata: MetadataResponse): SearchResponse()
    class SearchAccountResponse(val name: String, val icon: String): SearchResponse()
    class NftItemResponse(val address: String, val metadata: MetadataResponse): SearchResponse()
    class LocalSearchItemResponse(val nft_items: List<NftItemResponse>): SearchResponse()
    class AbstractSearchErrorMessage(val message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST ): SearchResponse()
}

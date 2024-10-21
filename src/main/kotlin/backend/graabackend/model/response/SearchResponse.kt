package backend.graabackend.model.response

import backend.graabackend.database.entities.GlobalSearchCollections
import backend.graabackend.database.entities.GlobalSearchNfts
import backend.graabackend.database.entities.Nfts
import org.springframework.http.HttpStatus

sealed class SearchResponse() {
    class SearchFinalResponse(
        val resultSearchAsAddress: MetadataResponse,
        val resultSearchAsName:  List<MetadataResponse>
    ): SearchResponse()

    class GetListOfSimilarNftsHelperResponse(val similarNfts: List<Nfts>): SearchResponse()
    class GetListOfSimilarGlobalNftsNftsHelperResponse(val similarNfts: List<GlobalSearchNfts>): SearchResponse()
    class GetListOfSimilarGlobalCollectionsHelperResponse(val similarCollections: List<GlobalSearchCollections>): SearchResponse()

    class GetListOfSimilar
    class MetadataResponse(
        val address: String,
        val name: String?,
        val image: String,
        val description: String?
    ): SearchResponse()
    class SearchItemResponse(val metadata: MetadataResponse): SearchResponse()
    class SearchAccountResponse(val name: String, val icon: String): SearchResponse()
    class NftItemResponse(var address: String, val metadata: MetadataResponse): SearchResponse()
    class LocalSearchItemResponse(val nft_items: List<NftItemResponse>): SearchResponse()
    class AbstractSearchErrorMessage(val message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST): SearchResponse()
}

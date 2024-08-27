package backend.graabackend.service.helpers

import backend.graabackend.database.dao.NftsDao
import backend.graabackend.database.entities.Nfts
import backend.graabackend.model.response.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory


// тут что-то странное - почему оно раблотает с двумя return
// проёбка тут - переделать надо тут всё по хорошему
private val logger: Logger = LoggerFactory.getLogger("backend.graabackend.service.helpers")

suspend fun callSearchMethod(
    arg: List<String>,
    callErrorMessage: String,
    funcErrorMessage: String,
    endpoint1: (suspend (String) -> SearchResponse.SearchItemResponse?)?,
    endpoint2: (suspend (String) -> SearchResponse.SearchAccountResponse?)?,
    nftsDao: NftsDao?
): SearchResponse =
    try {
        withContext(Dispatchers.IO) {
            if (endpoint1 != null) {
                logger.info("Calling endpoint1 with argument: $arg")
                val endpointResponse = arg[0]?.let { endpoint1(it) }
                if (endpointResponse != null) {
                    logger.info("Received successful response from endpoint1")
                    return@withContext SearchResponse.MetadataResponse(
                        name = endpointResponse.metadata.name,
                        image = endpointResponse.metadata.image,
                        description = endpointResponse.metadata.description
                    )
                } else {
                    logger.warn("Received null response from endpoint1")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(message = callErrorMessage)
                }
            }

            if (endpoint2 != null) {
                logger.info("Calling endpoint2 with argument: $arg")
                val endpointResponse = arg[0]?.let { endpoint2(it) }
                if (endpointResponse != null) {
                    logger.info("Received successful response from endpoint2")
                    return@withContext SearchResponse.SearchAccountResponse(name = endpointResponse.name, icon = endpointResponse.icon)
                } else {
                    logger.warn("Received null response from endpoint2")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(callErrorMessage)
                }
            }

            if (nftsDao != null) {
                logger.info("Calling endpoint3 with argument: $arg")
                val endpointResponse = arg[0]?.let { ownerAddress -> nftsDao.findAllyByNftOwnerAddress(nftOwnerAddress = ownerAddress) }
                if (endpointResponse != null) {
                    logger.info("Received successful response from database")
                    var resultSearchAsNftAddress: SearchResponse.MetadataResponse = SearchResponse.MetadataResponse(
                        name = "",
                        image = "",
                        description = ""
                    )

                    for (elem in endpointResponse) {
                        if (elem.nftAddress == arg[1]) {
                            resultSearchAsNftAddress = SearchResponse.MetadataResponse(
                                name = elem.nftName,
                                description = elem.nftDescription,
                                image = elem.nftImage
                            )
                        }
                    }

                    val helperResultSearchAsNftName =  SearchResponse.GetListOfSimilarNfts(
                        similarNfts = nftsDao.findByNameContaining(arg[1])
                    )

                    val resultSearchAsNftName: MutableList<SearchResponse.MetadataResponse> = mutableListOf()

                    for (elem in helperResultSearchAsNftName.similarNfts) {
                        resultSearchAsNftName.add(SearchResponse.MetadataResponse(
                            name = elem.nftName,
                            description = elem.nftDescription,
                            image = elem.nftImage
                            )
                        )
                    }

                    return@withContext SearchResponse.LocalSearchFinalResponse(
                        resultSearchAsNftAddress = resultSearchAsNftAddress,
                        resultSearchAsNftName = resultSearchAsNftName
                    )
                } else {
                    logger.warn("Received null response from endpoint3")
                    return@withContext SearchResponse.AbstractSearchErrorMessage(callErrorMessage)
                }
            }

            logger.error("No valid endpoint provided")
            return@withContext SearchResponse.AbstractSearchErrorMessage("Error: No valid endpoint provided")
        }
    } catch (e: Exception) {
        logger.error("Exception occurred: ${e.message}", e)
        SearchResponse.AbstractSearchErrorMessage(funcErrorMessage)
    }


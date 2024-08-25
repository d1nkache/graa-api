package backend.graabackend.service.helpers

import backend.graabackend.database.dao.NftsDao
import backend.graabackend.database.dao.VerifiedCollectionsDao
import backend.graabackend.model.response.CollectionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



suspend fun callCollectionMethod(
    firstArg: String?,
    secondArg: Boolean?,
    thirdArg: CollectionResponse.CollectionMetadataHelperResponse?,
    callErrorMessage: String,
    funcErrorMessage: String,
    endpoint1: suspend (String) -> CollectionResponse.NftItemsHelperResponse,
    verifiedCollectionsDao: VerifiedCollectionsDao,
    nftDao: NftsDao,
    pageNumber: Int,
    pageSize: Int
): CollectionResponse {
    return try {
        withContext(Dispatchers.IO) {
            println("Starting callCollectionMethod with firstArg: $firstArg, secondArg: $secondArg")

            if (firstArg != null) {
                val endpointResponse = endpoint1(firstArg)
                println("Received endpointResponse: $endpointResponse")

                if (secondArg != null) {
                    println("SecondArg is not null, but this condition is not implemented yet.")
                    return@withContext CollectionResponse.AbstractCollectionErrorMessage(message = "Not impl yet")
                } else {
                    val startIndex = pageNumber * pageSize
                    val paginatedItems = if (startIndex >= endpointResponse.nft_items.size) {
                        endpointResponse.nft_items
                    } else {
                        endpointResponse.nft_items.drop(startIndex).take(pageSize)
                    }
                    println(endpointResponse.nft_items.size)
                    println("Returning successful response with NFT items")
                    if (thirdArg != null) {
                        val collectionNfts = nftDao.findAllByCollectionAddress(firstArg)
                        var floorPrice = collectionNfts[0].nftTonPrice

                        for (elem in collectionNfts) {
                            if (elem.nftTonPrice < floorPrice) {
                                floorPrice = elem.nftTonPrice
                            }
                        }

                        return@withContext CollectionResponse.GetCollectionFinalResponse(
                            graaVerified = when(verifiedCollectionsDao.findVerifiedCollectionByCollectionAddress(collectionAddress = firstArg)?.collectionAddress) {
                                firstArg -> true
                                else -> false
                            },
                            floorPrice = floorPrice,
                            collectionMetadata = CollectionResponse.CollectionMetadataHelperResponse(
                                address = thirdArg.address,
                                metadata = thirdArg.metadata,
                                owner = thirdArg.owner,
                                approved_by = thirdArg.approved_by
                            ),
                            nftItems = paginatedItems
                        )
                    }
                    return@withContext CollectionResponse.AbstractCollectionErrorMessage(message = callErrorMessage)
                }
            } else {
                println("First argument is null, returning funcErrorMessage")
                return@withContext CollectionResponse.AbstractCollectionErrorMessage(callErrorMessage)
            }
        }
    } catch (e: Exception) {
        println("Exception occurred in callCollectionMethod: ${e.message}")
        return CollectionResponse.AbstractCollectionErrorMessage(funcErrorMessage)
    }
}

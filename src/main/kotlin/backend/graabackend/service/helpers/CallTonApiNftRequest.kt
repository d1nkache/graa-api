//package backend.graabackend.service.helpers
//
//import backend.graabackend.model.response.CollectionResponse
//import backend.graabackend.model.response.NftResponse
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//suspend fun callNftMethod(
//    arg: String,
//    callErrorMessage: String,
//    funcErrorMessage: String,
//    getCollectionInfoEndpoint: (suspend (String) -> CollectionResponse.CollectionMetadataHelperResponse?),
//    getNftInfoEndpoint: (suspend (String) -> NftResponse.NftMetadataHelperResponse?)
//): NftResponse =
//    try {
//        withContext(Dispatchers.IO) {
//            val getNftResponse = getNftInfoEndpoint(arg)
//                ?: return@withContext NftResponse.AbstractNftErrorMessage(message = callErrorMessage)
//
//            val getCollectionResponse = getCollectionInfoEndpoint(getNftResponse.collection.address)
//                ?: return@withContext NftResponse.AbstractNftErrorMessage(message = callErrorMessage)
//
//            return@withContext NftResponse.GetNftFinalResponse(
//                collectionMetadata = getCollectionResponse,
//                nftMetadata = getNftResponse
//            )
//        }
//    }
//    catch (e: Exception) {
//        println(e)
//        NftResponse.AbstractNftErrorMessage(message = funcErrorMessage)
//    }
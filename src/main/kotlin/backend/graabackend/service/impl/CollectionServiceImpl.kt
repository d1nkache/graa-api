package backend.graabackend.service.impl

import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.CollectionControllerTonApiEndpoints
import backend.graabackend.service.CollectionService
import backend.graabackend.service.SearchService
import backend.graabackend.service.helpers.callCollectionMethod
import backend.graabackend.service.helpers.callSearchMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CollectionServiceImpl : CollectionService {
    @Autowired
    lateinit var retrofitCollectionBuilder: RetrofitConfig

    protected val retrofitCollectionObject: CollectionControllerTonApiEndpoints by lazy {
        retrofitCollectionBuilder.buildCollectionRetrofitObject()
    }

    override suspend fun getCollection(collectionAddress: String): CollectionResponse = callCollectionMethod(
        firstArg = collectionAddress,
        secondArg = null,
        thirdArg = retrofitCollectionObject.getCollectionMetadata(collectionAddress),
        callErrorMessage = "",
        funcErrorMessage = "",
        endpoint1 = { address -> retrofitCollectionObject.getAllNftFromCollection(address) }
    )



    override suspend fun sortCollectionByPrice(ascending: Boolean, collectionAddress: String): CollectionResponse = callCollectionMethod(
        firstArg = collectionAddress,
        secondArg = ascending,
        thirdArg = null,
        callErrorMessage = "",
        funcErrorMessage = "",
        endpoint1 = {address -> retrofitCollectionObject.getAllNftFromCollection(address)},
    )
}
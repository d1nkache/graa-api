package backend.graabackend.service.impl

import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.CollectionControllerTonApiEndpoints
import backend.graabackend.service.CollectionService
import backend.graabackend.service.helpers.callCollectionMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CollectionServiceImpl : CollectionService {
    @Autowired
    lateinit var retrofitBuilder: RetrofitConfig

    private val retrofitObject: CollectionControllerTonApiEndpoints by lazy {
        retrofitBuilder.buildCollectionRetrofitObject()
    }

    override suspend fun getCollection(collectionAddress: String): CollectionResponse {
        val resp = callCollectionMethod(
            firstArg = collectionAddress,
            secondArg = null,
            callErrorMessage = "",
            funcErrorMessage = "",
            endpoint1 = { address -> retrofitObject.getAllNftFromCollection(address) }
        )
        println("resp - ${resp}")

        return resp
    }


    override suspend fun sortCollectionByPrice(ascending: Boolean, collectionAddress: String): CollectionResponse = callCollectionMethod(
        firstArg = collectionAddress,
        secondArg = ascending,
        callErrorMessage = "",
        funcErrorMessage = "",
        endpoint1 = {address -> retrofitObject.getAllNftFromCollection(address)},
    )
}
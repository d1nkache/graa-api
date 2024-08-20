package backend.graabackend.service.impl

import backend.graabackend.model.response.NftResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.NftControllerTonApiEndpoints
import backend.graabackend.service.NftService
import backend.graabackend.service.helpers.callNftMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NftServiceImpl() : NftService {
    @Autowired
    lateinit var retrofitNftBuilder: RetrofitConfig

    protected val retrofitCollectionObject: NftControllerTonApiEndpoints by lazy {
        retrofitNftBuilder.buildNftRetrofitObject()
    }

    override suspend fun getNft(nftAddress: String): NftResponse = callNftMethod(
        arg = nftAddress,
        callErrorMessage = "Nft collection not found for the given address",
        funcErrorMessage = "An error occurred while fetching the nft collection",
        getNftInfoEndpoint = { address -> retrofitCollectionObject.getNft(address) },
        getCollectionInfoEndpoint = { address -> retrofitCollectionObject.getCollectionMetadata(address) }
    )
}
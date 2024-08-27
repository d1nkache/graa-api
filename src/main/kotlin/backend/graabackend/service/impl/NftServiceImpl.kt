package backend.graabackend.service.impl

import kotlin.math.pow


import backend.graabackend.database.dao.NftsDao
import backend.graabackend.model.request.GraphqlRequest
import backend.graabackend.model.response.NftResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.NftControllerGraphqlEndpoints
import backend.graabackend.retrofit.endpoints.NftControllerTonApiEndpoints
import backend.graabackend.service.NftService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NftServiceImpl(
    private val nftsDao: NftsDao
): NftService {
    @Autowired
    lateinit var retrofitNftBuilder: RetrofitConfig

    protected val retrofitNftGraphqlObject: NftControllerGraphqlEndpoints by lazy {
        retrofitNftBuilder.buildNftGetGemsRetrofitObject()
    }
    protected val retrofitNftObject: NftControllerTonApiEndpoints by lazy {
        retrofitNftBuilder.buildNftRetrofitObject()
    }
    val tonDecimals: Long = 10.0.pow(9).toLong()

    override suspend fun getNft(nftAddress: String): NftResponse {
        try {
            return withContext(Dispatchers.IO) {
                val getNftResponse = retrofitNftObject.getNft(nftAddress)
                val getCollectionResponse = retrofitNftObject.getCollectionMetadata(getNftResponse.collection.address)
                val query =
                    """
                    query AlphaNftItemByAddress(${'$'}address: String!) {
                      alphaNftItemByAddress(address: ${'$'}address) {
                        attributes {
                          traitType
                          value
                        }
                      }
                    }
                    """.trimIndent()

                val nftAttributes = retrofitNftGraphqlObject.executeGraphqlQuery(GraphqlRequest(
                    query = query,
                    variables = mapOf("address" to nftAddress)
                )).data.alphaNftItemByAddress.attributes

                println(nftAttributes.size)
                return@withContext NftResponse.GetNftFinalResponse(
                    collectionMetadata = getCollectionResponse,
                    nftMetadata = getNftResponse,
                    nftAttributes = nftAttributes
                )
            }
        }
        catch (e: Exception) {
            return NftResponse.AbstractNftErrorMessage(message = "$e")
        }
    }

    override suspend fun updateNftPrice(nftAddress: String): NftResponse {
        return withContext(Dispatchers.IO) {
            var lastNftResponse: NftResponse.GetNftSmartContractInfoFinalResponse
            val endTime = System.currentTimeMillis() + 4 * 60 * 1000
            var nftOwnerAddress: String
            val fixedPriceNum = "0x46495850"

            while (System.currentTimeMillis() < endTime) {
                try {
                    nftOwnerAddress = retrofitNftObject.getNft(nftAddress).owner.address
                    lastNftResponse = retrofitNftObject.getNftSaleSmartContract(nftOwnerAddress)

                    if (lastNftResponse.success) {
                        if (lastNftResponse.stack[0].num == fixedPriceNum) {
                            val currentNftEntity = nftsDao.findEntityByNftAddress(nftAddress)
                                ?: return@withContext NftResponse.AbstractNftErrorMessage(message = "Error: This nft not found in database")
                            currentNftEntity.nftTonPrice = lastNftResponse.decoded.full_price.toLong().apply {
                                if (this == 0L) currentNftEntity.nftTonPrice = -1
                            }
                            nftsDao.save(currentNftEntity)

                            return@withContext lastNftResponse
                        }
                        else {
                            return@withContext NftResponse.AbstractNftErrorMessage(message = "This nft is up for auction")
                        }
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }

                delay(5000L)
            }

            return@withContext NftResponse.AbstractNftErrorMessage("Error: Sale Smart Contract was not deploy successfully")
        }
    }
}

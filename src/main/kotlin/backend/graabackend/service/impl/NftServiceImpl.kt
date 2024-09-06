package backend.graabackend.service.impl

import backend.graabackend.database.dao.NftsDao
import backend.graabackend.model.mapper.NftMapper
import backend.graabackend.model.request.GraphqlRequest
import backend.graabackend.model.response.NftResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.NftControllerGraphqlEndpoints
import backend.graabackend.retrofit.endpoints.NftControllerTonApiEndpoints
import backend.graabackend.service.NftService
import backend.graabackend.service.helpers.extractBits
import ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class NftServiceImpl(
    private val nftsDao: NftsDao,
    private val nftMapper: NftMapper
): NftService {
    @Autowired
    lateinit var retrofitNftBuilder: RetrofitConfig

    protected val retrofitNftGraphqlObject: NftControllerGraphqlEndpoints by lazy {
        retrofitNftBuilder.buildNftGetGemsRetrofitObject()
    }
    protected val retrofitNftObject: NftControllerTonApiEndpoints by lazy {
        retrofitNftBuilder.buildNftRetrofitObject()
    }
//    val tonDecimals: Long = 10.0.pow(9).toLong()

    override suspend fun getNft(nftAddress: String): NftResponse {
        try {
            val nftPrice = withContext(Dispatchers.IO) {
                nftsDao.findEntityByNftAddress(nftAddress)
            }?.nftTonPrice
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
            val getNftResponse = retrofitNftObject.getNft(nftAddress)
            val getCollectionResponse = retrofitNftObject.getCollectionMetadata(getNftResponse.collection.address)
            val nftAttributes = retrofitNftGraphqlObject.executeGraphqlQuery(
                GraphqlRequest(
                    query = query,
                    variables = mapOf("address" to nftAddress)
                )
            ).data.alphaNftItemByAddress.attributes

            return NftResponse.GetNftFinalResponse(
                saleMetadata = nftMapper.asSaleMetadataHelperResponse(nftPrice),
                collectionMetadata = getCollectionResponse,
                nftMetadata = getNftResponse,
                nftAttributes = nftAttributes
            )
        }
        catch (ex: Exception) {
            println(ex.message)
            return NftResponse.AbstractNftErrorMessage(
                message = "Error in NftService: $ex",
                status = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    override suspend fun updateNftPrice(nftAddress: String): NftResponse {
        try {
            var lastNftResponse: NftResponse.GetNftSmartContractInfoFinalResponse
            val endTime = System.currentTimeMillis() + 1.5 * 60 * 1000
            val fixedPriceNum = "0x46495850"
            var nftOwnerAddress: String

            while (System.currentTimeMillis() < endTime) {
                nftOwnerAddress = retrofitNftObject.getNft(nftAddress).owner.address
                lastNftResponse = retrofitNftObject.getNftSaleSmartContract(nftOwnerAddress)

                if (lastNftResponse.success) {
                    val stackItem = lastNftResponse.stack.firstOrNull()

                    if (stackItem?.num == fixedPriceNum) {
                        val currentNftEntity = withContext(Dispatchers.IO) {
                            nftsDao.findEntityByNftAddress(nftAddress)
                        }
                            ?: return NftResponse.AbstractNftErrorMessage(message = "Error: This NFT not found in the database")

                        val fullPrice = lastNftResponse.decoded.full_price.toLong()
                        currentNftEntity.nftTonPrice = if (fullPrice == 0L) -1 else fullPrice

                        withContext(Dispatchers.IO) {
                            nftsDao.save(currentNftEntity)
                        }

                        return lastNftResponse
                    } else {
                        return NftResponse.AbstractNftErrorMessage(message = "This NFT is up for auction")
                    }
                }
            }
            delay(15000L)

            return NftResponse.AbstractNftErrorMessage(message = "This nft is up for auction")
        }
        catch(ex: Exception) {
            println(ex.message)
            return NftResponse.AbstractNftErrorMessage(
                message = "Error in NftService: $ex",
                status = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    override suspend fun sellNft(nftAddress: String, transactionHash: String): NftResponse {
        val byteArray = hexStringToByteArray(transactionHash)
        val first4Bits = extractBits(byteArray, 0, 4)
        val result = first4Bits and 1

        if (result == 1) {
            val dealNft = withContext(Dispatchers.IO) {
                nftsDao.findEntityByNftAddress(nftAddress = nftAddress)
            }

            if (dealNft != null) {
                dealNft.nftTonPrice = -1
            }

            return NftResponse.AbstractNftErrorMessage("Transaction has success status", status = HttpStatus.OK)
        }
        else {
            return NftResponse.AbstractNftErrorMessage("Transaction was not success")
        }
    }
}

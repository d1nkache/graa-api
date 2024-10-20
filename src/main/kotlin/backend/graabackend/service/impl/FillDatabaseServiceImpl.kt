package backend.graabackend.service.impl

import backend.graabackend.controller.helpers.CollectionControllerHelper
import backend.graabackend.controller.helpers.NftControllerHelper

import backend.graabackend.database.entities.GlobalSearchCollections
import backend.graabackend.database.dao.GlobalSearchCollectionsDao
import backend.graabackend.database.entities.GlobalSearchNfts
import backend.graabackend.database.dao.GlobalSearchNftsDao

import backend.graabackend.model.response.FillDatabaseResponse

import backend.graabackend.retrofit.endpoints.FillDatabaseTonApiEndpoints
import backend.graabackend.retrofit.RetrofitConfig

import backend.graabackend.service.FillDatabaseService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FillDatabaseServiceImpl(
    private val nftDao: GlobalSearchNftsDao,
    private val nftControllerHelper: NftControllerHelper,
    private val collectionDao: GlobalSearchCollectionsDao,
    private val collectionControllerHelper: CollectionControllerHelper,
): FillDatabaseService {
    @Autowired
    lateinit var retrofitFillDatabaseBuilder: RetrofitConfig
    protected val retrofitFillDatabaseObject: FillDatabaseTonApiEndpoints by lazy {
        retrofitFillDatabaseBuilder.buildFillDatabaseRetrofitObject()
    }

    override fun addCollectionsInDatabase(addresses: List<String>): FillDatabaseResponse {
        var counter: Int = 0

        try {
            for (address in addresses) {
                retrofitFillDatabaseObject.getCollectionMetadata(accountId = address).apply {
                    val currentObject = this.execute().body()

                    counter += 1
                    println("[CURRENT ITER] --> [$counter/${addresses.size}]")
                    val collectionName = currentObject?.metadata?.name ?: ""
                    val collectionDescription = currentObject?.metadata?.description ?: ""
                    val collectionAddress = currentObject?.address ?: ""
                    val collectionImage = currentObject?.metadata?.image ?: ""

                    collectionDao.save(
                        GlobalSearchCollections(
                            collectionAddress = nftControllerHelper.changeNftAddressFormat(collectionAddress),
                            collectionName = collectionName,
                            collectionDescription = collectionDescription,
                            collectionImage = collectionImage
                        )
                    )
                }
            }

            return FillDatabaseResponse(
                answer = "Added $counter NFTs in database",
            )
        }
        catch (ex: Exception) {
            return FillDatabaseResponse(
                answer = "Error: ${ex.message}",
            )
        }
    }

    override fun addNftInDatabase(addresses: List<String>): FillDatabaseResponse {
        var collectionCounter: Int = 0
        var nftCounter: Int = 0

        try {
            for (address in addresses) {
                collectionCounter += 1
                println("[CURRENT COLLECTION ITER] --> [$collectionCounter/${addresses.size}]")
                val collectionNfts =
                    retrofitFillDatabaseObject.getAllNftFromCollection(accountId = address).execute().body()?.nft_items

                if (collectionNfts != null) {
                    for (nft in collectionNfts) {
                        Thread.sleep(250L)

                        retrofitFillDatabaseObject.getNft(accountId = nft.address).apply {
                            val currentObject = this.execute().body()
                            val nftName = currentObject?.metadata?.name ?: ""
                            val nftDescription = currentObject?.metadata?.description ?: ""
                            val nftOwner = currentObject?.owner?.address ?: ""
                            val nftImage = currentObject?.metadata?.image ?: ""
                            nftCounter += 1
                            println("[CURRENT NFT ITER] --> [$nftCounter/${collectionNfts.size}] --> [ADDRESS] --> [${nft.address}]")

                            nftDao.save(
                                GlobalSearchNfts(
                                    nftAddress = nftControllerHelper.changeNftAddressFormat(
                                        collectionControllerHelper.changeCollectionAddressFormat(
                                            nft.address
                                        )
                                    ),
                                    nftName = nftName,
                                    nftDescription = nftDescription,
                                    nftOwner = nftOwner,
                                    nftImage = nftImage,
                                    nftCollection = address,

                                )
                            )
                        }
                    }
                }
                nftCounter = 0
            }

            return FillDatabaseResponse(
                answer = "NFTs have been added in database success",
            )
        }
        catch(ex: Exception) {
            return FillDatabaseResponse(
                answer = "Error: ${ex.message}",
            )
        }
    }
}
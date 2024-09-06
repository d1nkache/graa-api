package backend.graabackend.service.impl

import backend.graabackend.retrofit.endpoints.UserControllerGraphqlEndpoints
import backend.graabackend.model.request.NftItemsByOwnerRequest
import backend.graabackend.model.response.UserResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.model.request.Variables
import backend.graabackend.model.mapper.UserMapper
import backend.graabackend.database.entities.Nfts
import backend.graabackend.database.dao.NftsDao
import backend.graabackend.service.UserService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus

import org.slf4j.LoggerFactory
import org.slf4j.Logger

@Service
class UserServiceImpl(
    private val nftsDao: NftsDao,
    private val userMapper: UserMapper
) : UserService {
    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
    // убрать запрос в другой файл
    val query =
        """
                query NftItemsByOwner(${'$'}ownerAddress: String!, ${'$'}first: Int!) {
                  nftItemsByOwner(ownerAddress: ${'$'}ownerAddress, first: ${'$'}first) {
                    items {
                      address
                      collection {
                        address
                      }
                      name
                      description
                      content {
                        ... on NftContentImage {
                          image {
                            baseUrl
                          }
                        }
                      }x
                    }
                  }
                }
            """.trimIndent()
    @Autowired
    lateinit var retrofitUserBuilder: RetrofitConfig

    protected val retrofitUserGraphqlObject: UserControllerGraphqlEndpoints by lazy {
        retrofitUserBuilder.buildUserGetGemsRetrofitObject()
    }

    @Transactional
    override suspend fun addNewUser(walletAddress: String): UserResponse {
        val newUserNfts: MutableList<Nfts> = mutableListOf()

        try {
            retrofitUserGraphqlObject.executeGraphqlQuery(
                NftItemsByOwnerRequest(
                    query = query,
                    variables = Variables(
                        first = 100,
                        ownerAddress = walletAddress
                    )
                )
            ).data.nftItemsByOwner.items.forEach { nftItem ->
                userMapper.asNftsEntity(nftItem = nftItem, walletAddress = walletAddress).also { newUserNft ->
                    nftsDao.findEntityByNftAddress(nftAddress = nftItem.address).also {
                        if (it == null) {
                            nftsDao.save(newUserNft)
                            newUserNfts.add(newUserNft)
                        }
                        else {
                            return UserResponse.AbstractUserErrorMessage(
                                message = "User already exists",
                                status = HttpStatus.OK
                            )
                        }
                    }
                }
            }

            return UserResponse.NewOrUpdatedUserNftsFinalResponse(newNfts = newUserNfts)
        }
        catch(ex: Exception) {
            logger.error(ex.message)
            return UserResponse.AbstractUserErrorMessage(
                message = "Error: Something went wrong",
                status = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    @Transactional
    @Modifying
    override suspend fun updateUserInfo(walletAddress: String): UserResponse {
        try {
            val userCurrentNfts = withContext(Dispatchers.IO) {
                nftsDao.findAllyByNftOwnerAddress(walletAddress)
            }
            val userActualNfts = retrofitUserGraphqlObject.executeGraphqlQuery(
                NftItemsByOwnerRequest(
                    query = query,
                    variables = Variables(
                        first = 100,
                        ownerAddress = walletAddress
                    )
                )
            ).data.nftItemsByOwner.items
            val newUserNfts: MutableList<Nfts> = mutableListOf()
            val sameUserNfts: MutableList<String> = mutableListOf()

            if (userCurrentNfts.isEmpty()) return addNewUser(walletAddress)

            for (elem in userActualNfts) {
                for (counter in userCurrentNfts.indices) {
                    if ((elem.collection.address ==  userCurrentNfts[counter].collectionAddress) and (elem.address == userCurrentNfts[counter].nftAddress)) {
                        sameUserNfts.add(elem.address)
                        if (userCurrentNfts[counter].nftOwnerAddress != elem.address) userCurrentNfts[counter].nftOwnerAddress = walletAddress
                        if (userCurrentNfts[counter].nftAddress != elem.address) userCurrentNfts[counter].nftAddress = elem.address
                        if (userCurrentNfts[counter].nftName != elem.name)  userCurrentNfts[counter].nftName = elem.name
                    }
                }
            }

            for (elem in userActualNfts) {
                if (elem.address !in sameUserNfts) {
                    withContext(Dispatchers.IO) {
                        userMapper.asNftsEntity(nftItem = elem, walletAddress = walletAddress).apply {
                            nftsDao.save(this)
                            newUserNfts.add(this)
                        }
                    }
                }
            }

            return UserResponse.NewOrUpdatedUserNftsFinalResponse(newNfts = newUserNfts)
        }
        catch(ex: Exception) {
            println(ex.message)
            return UserResponse.AbstractUserErrorMessage(message = "Error: Something went wrong")
        }
    }
}
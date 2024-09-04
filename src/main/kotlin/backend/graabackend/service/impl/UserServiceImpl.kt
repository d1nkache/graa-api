package backend.graabackend.service.impl

import backend.graabackend.database.dao.NftsDao
import backend.graabackend.database.entities.Nfts
import backend.graabackend.model.mapper.UserMapper
import backend.graabackend.model.response.UserResponse
import backend.graabackend.retrofit.RetrofitConfig
import backend.graabackend.retrofit.endpoints.UserControllerTonApiEndpoints
import backend.graabackend.service.UserService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserServiceImpl(
    private val nftsDao: NftsDao,
    private val userMapper: UserMapper
) : UserService {
    @Autowired
    lateinit var retrofitUserBuilder: RetrofitConfig

    protected val retrofitUserObject: UserControllerTonApiEndpoints by lazy {
        retrofitUserBuilder.buildUserRetrofitObject()
    }

    @Transactional
    override suspend fun addNewUser(walletAddress: String): UserResponse {
        val newUserNfts: MutableList<Nfts> = mutableListOf()

        try {
            retrofitUserObject.getUserNfts(walletAddress)?.nft_items?.forEach { nftItem ->
                userMapper.asNftsEntity(nftItem = nftItem).also { newUserNft ->
                    nftsDao.save(newUserNft)
                    newUserNfts.add(newUserNft)
                }
            }

            return UserResponse.NewOrUpdatedUserNftsFinalResponse(newNfts = newUserNfts)
        }
        catch(ex: Exception) {
            println(ex.message)
            return UserResponse.AbstractUserErrorMessage(message = "Error: Something went wrong")
        }
    }

    @Transactional
    @Modifying
    override suspend fun updateUserInfo(walletAddress: String): UserResponse {
        try {
            val userCurrentNfts = withContext(Dispatchers.IO) {
                nftsDao.findAllyByNftOwnerAddress(walletAddress)
            }
            val userActualNfts = retrofitUserObject.getUserNfts(walletAddress)?.nft_items
            val newUserNfts: MutableList<Nfts> = mutableListOf()
            val sameUserNfts: MutableList<String> = mutableListOf()

            if (userActualNfts != null) {
                if (userCurrentNfts.isEmpty() and userActualNfts.isNotEmpty()) return addNewUser(walletAddress)
            }

            if (userActualNfts != null) {
                for (elem in userActualNfts) {
                    for (counter in userCurrentNfts.indices) {
                        if ((elem.collection?.address ==  userCurrentNfts[counter].collectionAddress) and (elem.address == userCurrentNfts[counter].nftAddress)) {
                            sameUserNfts.add(elem.address)
                            if (userCurrentNfts[counter].nftOwnerAddress != elem.owner.address) userCurrentNfts[counter].nftOwnerAddress = elem.owner.address
                            if (userCurrentNfts[counter].nftAddress != elem.address) userCurrentNfts[counter].nftAddress = elem.address
                            if (userCurrentNfts[counter].nftName != elem.metadata.name)  userCurrentNfts[counter].nftName = elem.metadata.name
                        }
                    }
                }

                for (elem in userActualNfts) {
                    if (elem.address !in sameUserNfts) {
                        withContext(Dispatchers.IO) {
                            userMapper.asNftsEntity(elem).apply {
                                nftsDao.save(this)
                                newUserNfts.add(this)
                            }
                        }
                    }
                }
            }
            else {
                withContext(Dispatchers.IO) {
                    nftsDao.deleteAllByNftOwnerAddress(walletAddress)
                }

                return UserResponse.AbstractUserErrorMessage(
                    message = "All nfts with owner address: $walletAddress was successfully deleted",
                    status = HttpStatus.OK
                )
            }

            return UserResponse.NewOrUpdatedUserNftsFinalResponse(newNfts = newUserNfts)
        }
        catch(ex: Exception) {
            println(ex.message)
            return UserResponse.AbstractUserErrorMessage(message = "Error: Something went wrong")
        }
    }
}
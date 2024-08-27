package backend.graabackend.service

import backend.graabackend.model.response.NftResponse

interface NftService {
    suspend fun getNft(nftAddress: String): NftResponse
    suspend fun updateNftPrice(nftAddress: String): NftResponse
    suspend fun sellNft(nftAddress: String, transactionHash: String): NftResponse
}
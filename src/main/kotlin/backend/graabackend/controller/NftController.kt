package backend.graabackend.controller

import backend.graabackend.model.response.NftResponse

interface NftController {
    suspend fun getNft(nftAddress: String): NftResponse
    suspend fun updateNftPrice(nftAddress: String): NftResponse
    suspend fun sellNft(nftAddress: String, transactionHash: String): NftResponse
}
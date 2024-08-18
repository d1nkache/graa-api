package backend.graabackend.controller

import backend.graabackend.model.response.NftResponse

interface NftController {
    suspend fun getNft(nftAddress: String): NftResponse
}
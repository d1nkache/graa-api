package backend.graabackend.controller.impl

import backend.graabackend.controller.NftController
import backend.graabackend.controller.helpers.nftControllerHelper
import backend.graabackend.model.response.NftResponse
import backend.graabackend.service.NftService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/nft")
class NftControllerImpl(
    private val nftService: NftService
) : NftController {
    @GetMapping("/get/{nftAddress}")
    override suspend fun getNft(@PathVariable nftAddress: String): NftResponse = nftControllerHelper(
        firstArg = nftAddress,
        errorMessage = "",
        serviceMethod1 = nftService::getNft
    )
}
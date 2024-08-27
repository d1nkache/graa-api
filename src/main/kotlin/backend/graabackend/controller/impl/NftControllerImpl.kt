package backend.graabackend.controller.impl

import backend.graabackend.controller.NftController
import backend.graabackend.controller.helpers.nftControllerHelper
import backend.graabackend.model.response.NftResponse
import backend.graabackend.service.NftService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/nft")
class NftControllerImpl(
    private val nftService: NftService
) : NftController {
    @GetMapping("/get/{nftAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun getNft(@PathVariable nftAddress: String): NftResponse = nftControllerHelper(
        firstArg = nftAddress,
        errorMessage = "",
        serviceMethod1 = nftService::getNft,
    )

    @PostMapping("/updateNftPrice/{nftAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun updateNftPrice(@PathVariable nftAddress: String): NftResponse = nftService.updateNftPrice(nftAddress)

    @PostMapping("/sell-nft/{nftAddress}/{transactionHash}")
    override suspend fun sellNft(@PathVariable nftAddress: String, @PathVariable transactionHash: String): NftResponse =
        nftService.sellNft(nftAddress, transactionHash)
}
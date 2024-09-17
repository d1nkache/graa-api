package backend.graabackend.controller.impl

import backend.graabackend.controller.helpers.NftControllerHelper
import backend.graabackend.model.response.NftResponse
import backend.graabackend.controller.NftController
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/nft")
class NftControllerImpl(
    private val nftControllerHelper: NftControllerHelper
) : NftController {
    @GetMapping("/get/{nftAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun getNft(@PathVariable nftAddress: String): NftResponse {
        val hexNftAddress: String = nftControllerHelper.changeNftAddressFormat(nftAddress = nftAddress)

        return nftControllerHelper.checkControllerVariablesOnError(
            nftAddress = hexNftAddress,
            transactionHash = null,
            methodName = "getNft"
        )
    }

    @PostMapping("/updateNftPrice/{nftAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun updateNftPrice(@PathVariable nftAddress: String): NftResponse {
        val hexNftAddress: String = nftControllerHelper.changeNftAddressFormat(nftAddress = nftAddress)

        return nftControllerHelper.checkControllerVariablesOnError(
            nftAddress = hexNftAddress,
            transactionHash = null,
            methodName = "updateNftPrice"
        )
    }


    @PostMapping("/sell-nft/{nftAddress}/{boc}")
    override suspend fun sellNft(@PathVariable nftAddress: String, @PathVariable boc: String): NftResponse {
        val hexNftAddress: String = nftControllerHelper.changeNftAddressFormat(nftAddress = nftAddress)
        val transactionHash: String = nftControllerHelper.convertBocToTransactionHash(boc = boc)

        return nftControllerHelper.checkControllerVariablesOnError(
            nftAddress = hexNftAddress,
            transactionHash = transactionHash,
            methodName = "updateNftPrice"
        )
    }
}
package backend.graabackend.controller.impl

import backend.graabackend.controller.FillDatabaseController
import backend.graabackend.model.request.AddAddresses
import backend.graabackend.model.response.FillDatabaseResponse
import backend.graabackend.service.FillDatabaseService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/fill-database")
class FillDatabaseControllerImpl(
    private val fillDatabaseService: FillDatabaseService,
) : FillDatabaseController {
    @PostMapping("/insert-collections")
    override fun addCollectionsInDatabase(@RequestBody addresses: AddAddresses): FillDatabaseResponse {
        return fillDatabaseService.addCollectionsInDatabase(addresses = addresses.addresses)
    }

    @PostMapping("/insert-nfts")
    override fun addNftInDatabase(@RequestBody addresses: AddAddresses): FillDatabaseResponse {
        return fillDatabaseService.addNftInDatabase(addresses = addresses.addresses)
    }
}


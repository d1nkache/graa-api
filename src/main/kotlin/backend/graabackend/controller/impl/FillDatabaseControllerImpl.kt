package backend.graabackend.controller.impl

import backend.graabackend.controller.FillDatabaseController
import backend.graabackend.model.request.AddAddresses
import backend.graabackend.model.response.FillDatabaseResponse
import backend.graabackend.service.FillDatabaseService
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@RestController
@RequestMapping("/fill-database")
class FillDatabaseControllerImpl(
    private val fillDatabaseService: FillDatabaseService,
    private val webClientBuilder: WebClient.Builder
) : FillDatabaseController {
    private val webClient = webClientBuilder.baseUrl("http://localhost:8000/fill-database").build()

    @PostMapping ("/fill-gateway/{item}")
    fun addItem(@PathVariable item: String, @RequestBody request: AddAddresses): FillDatabaseResponse {
        return FillDatabaseResponse(webClient.post()
            .uri("/insert-nfts")
            .bodyValue(request)
            .header("Authorization", "Bearer KUpuNRJJ4qo?S5IqE/xob9hGt1G8LdM!IazoT/l!vxrvLuFmSJw/bmSUXDosyd2o1Yv/1i12e3xrL7le/Rw233MJWf8InhucuBk1Uyok7aPZsM-UDd6jeF5p-DiVO-6DQZ9Cnzdmq/p DqNuhUCFko/hOXuIUuQz21sixhYh4U=!jc1jl-YFl9VnM4NXhc0YPNCA571daCZnycRvsG9?Cu8F5kCnoMPew6hv5qzxylaTz-iBAwUSgbxYHxj0H3oE?")
            .retrieve()
            .bodyToMono(String::class.java)
            .timeout(Duration.ofSeconds(6000))
            .block() ?: "Error: Something went wrong"
        )
    }

    @PostMapping("/insert-collections")
    override fun addCollectionsInDatabase(@RequestBody addresses: AddAddresses): FillDatabaseResponse {
        return fillDatabaseService.addCollectionsInDatabase(addresses = addresses.addresses)
    }

    @PostMapping("/insert-nfts")
    override fun addNftInDatabase(@RequestBody addresses: AddAddresses): FillDatabaseResponse {
        return fillDatabaseService.addNftInDatabase(addresses = addresses.addresses)
    }
}


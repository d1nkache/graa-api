package backend.graabackend.controller.impl

import backend.graabackend.controller.HealthController
import backend.graabackend.model.message.HealthMessage
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthControllerImpl : HealthController {
    @GetMapping("/health")
    @CrossOrigin(origins = ["*"], maxAge = 3600)
    override fun getServerHealthState(): HealthMessage = HealthMessage()
}
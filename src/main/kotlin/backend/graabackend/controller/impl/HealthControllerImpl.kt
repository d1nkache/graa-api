package backend.graabackend.controller.impl

import backend.graabackend.controller.HealthController
import backend.graabackend.model.message.HealthMessage

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping

@RestController
class HealthControllerImpl : HealthController {
    @GetMapping("/health")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override fun getServerHealthState(): HealthMessage = HealthMessage()
}
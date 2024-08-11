package backend.graabackend.controller

import backend.graabackend.model.message.HealthMessage

interface HealthController {
    fun getServerHealthState(): HealthMessage

}
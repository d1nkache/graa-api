package backend.graabackend.database.entities

import backend.graabackend.database.AbstractEntity

import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class VerifiedCollections (
    @Column
    val collectionName: String?,

    @Column(nullable = false)
    val collectionAddress: String,

    @Column(nullable = false)
    val ownerAddress: String

): AbstractEntity()

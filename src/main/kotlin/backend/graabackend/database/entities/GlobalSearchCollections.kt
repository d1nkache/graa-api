package backend.graabackend.database.entities

import backend.graabackend.database.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity


@Entity
class GlobalSearchCollections(
    @Column
    val collectionAddress: String,

    @Column
    val collectionName: String,

    @Column
    val collectionDescription: String,

    @Column
    val collectionImage: String
): AbstractEntity()
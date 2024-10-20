package backend.graabackend.database.entities

import backend.graabackend.database.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class GlobalSearchNfts (
    @Column
    val nftAddress: String,

    @Column
    val nftName: String,

    @Column
    val nftDescription: String,

    @Column
    val nftOwner: String,

    @Column
    val nftImage: String,

    @Column
    val nftCollection: String
): AbstractEntity()

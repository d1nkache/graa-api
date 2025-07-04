package backend.graabackend.database.entities

import backend.graabackend.database.AbstractEntity

import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class Nfts (
    @Column
    var nftAddress: String,

    @Column(nullable = true)
    var collectionAddress: String?,

    @Column(nullable = true)
    var nftName: String?,

    @Column(nullable = true)
    var nftDescription: String?,

    @Column
    var nftImage: String,

    @Column
    var nftOwnerAddress: String,

    @Column
    var nftTonPrice: Long
): AbstractEntity()
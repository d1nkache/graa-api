package backend.graabackend.database.dao

import backend.graabackend.database.entities.Nfts
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface NftsDao: CommonDao<Nfts> {
    fun findEntityByNftAddress(nftAddress: String): Nfts?
    fun findAllyByNftOwnerAddress(nftOwnerAddress: String): List<Nfts>
    fun deleteAllByNftOwnerAddress(nftOwnerAddress: String)
    fun findAllByCollectionAddress(collectionAddress: String): List<Nfts>

    @Query("SELECT e FROM Nfts e WHERE e.nftName LIKE %:nftName%")
    fun findByNameContaining(@Param("nftName") name: String): List<Nfts>
}

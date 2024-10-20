package backend.graabackend.database.dao

import backend.graabackend.database.entities.GlobalSearchNfts
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query

interface GlobalSearchNftsDao: CommonDao<GlobalSearchNfts> {
    fun findByNftCollection(nftCollection: String): List<GlobalSearchNfts>

    @Query("SELECT e FROM GlobalSearchNfts e WHERE e.nftName LIKE %:nftName%")
    fun findByNameContaining(@Param("nftName") name: String): List<GlobalSearchNfts>
}
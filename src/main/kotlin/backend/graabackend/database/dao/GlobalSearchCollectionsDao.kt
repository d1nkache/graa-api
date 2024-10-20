package backend.graabackend.database.dao

import backend.graabackend.database.entities.GlobalSearchCollections
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query
import backend.graabackend.database.entities.Nfts

interface GlobalSearchCollectionsDao: CommonDao<GlobalSearchCollections> {
    fun findByCollectionAddress(collectionAddress: String): List<GlobalSearchCollections>

    @Query("SELECT e FROM GlobalSearchCollections e WHERE e.collectionName LIKE %:collectionName%")
    fun findByNameContaining(@Param("collectionName") name: String): List<GlobalSearchCollections>
}
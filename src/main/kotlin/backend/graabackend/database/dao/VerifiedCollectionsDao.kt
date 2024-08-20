package backend.graabackend.database.dao

import backend.graabackend.database.entities.VerifiedCollections

interface VerifiedCollectionsDao: CommonDao<VerifiedCollections> {
    fun findVerifiedCollectionByCollectionAddress(collectionAddress: String): VerifiedCollections?
    fun deleteByCollectionAddress(collectionAddress: String): Int
}
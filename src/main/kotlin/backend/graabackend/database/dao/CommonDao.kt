package backend.graabackend.database.dao

import backend.graabackend.database.AbstractEntity
import org.springframework.data.repository.CrudRepository

interface CommonDao<T: AbstractEntity>: CrudRepository<T, Long>
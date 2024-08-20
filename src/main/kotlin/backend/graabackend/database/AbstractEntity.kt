package backend.graabackend.database

import jakarta.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column
    val createdAt: LocalDateTime = LocalDateTime.now()
}
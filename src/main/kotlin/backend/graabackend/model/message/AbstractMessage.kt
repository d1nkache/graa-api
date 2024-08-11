package backend.graabackend.model.message
import org.springframework.http.HttpStatus

abstract class AbstractMessage {
    val message: String = "Server is OK"
    val status: HttpStatus = HttpStatus.OK
}
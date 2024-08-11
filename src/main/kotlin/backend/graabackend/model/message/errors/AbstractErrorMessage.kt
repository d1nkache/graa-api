package backend.graabackend.model.message.errors

import org.springframework.http.HttpStatus

abstract class AbstractErrorMessage {
    var extraComment: String  = ""
    val message: String = "Error: $extraComment"
    val statusCode: HttpStatus = HttpStatus.BAD_REQUEST

}
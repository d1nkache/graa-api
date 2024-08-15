package backend.graabackend.security

import io.github.cdimascio.dotenv.dotenv
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthHeaderFilter : OncePerRequestFilter() {


    companion object {
        private val dotenv = dotenv {
            directory = "/app"
            filename = ".env"
        }
        private const val AUTH_HEADER = "Authorization"
        private val API_AUTH_TOKEN = dotenv["API_AUTH_TOKEN"]
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader(AUTH_HEADER)
        val token = authHeader?.getTokenValue()
        println(token)

        if (token == API_AUTH_TOKEN) {
            val authToken = UsernamePasswordAuthenticationToken("authenticated", null, listOf(SimpleGrantedAuthority("ROLE_USER")))
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authToken
            println("User authenticated")
        }

        filterChain.doFilter(request, response)
    }

    private fun String?.getTokenValue(): String? {
        return this?.takeIf { it.startsWith("Bearer ") }?.substringAfter("Bearer ")
    }
}

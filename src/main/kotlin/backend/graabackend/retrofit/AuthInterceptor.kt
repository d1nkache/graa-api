package backend.graabackend.retrofit

import okhttp3.Interceptor

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
//        println("Request URL: ${request.url}")
//        println("Request Headers: ${request.headers}")

        val response = chain.proceed(request)
//        if ((response.code in 400..499) || (response.code in 500..599))

//        println("Response Code: ${response.code}")
//        println("Response Body: ${response.body?.string()}")

        return chain.proceed(request)
    }
}
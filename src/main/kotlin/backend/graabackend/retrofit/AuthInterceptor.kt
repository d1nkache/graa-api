//package backend.graabackend.retrofit
//
//import okhttp3.Interceptor
//
//class AuthInterceptor(private val authToken: String) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//        val request = chain.request()
//        println("Request URL: ${request.url}")
//        println("Request Headers: ${request.headers}")
//
//        val response = chain.proceed(request)
//
//        println("Response Code: ${response.code}")
//        println("Response Body: ${response.body?.string()}")
//
//        return chain.proceed(request)
//    }
//}
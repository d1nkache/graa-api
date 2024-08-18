package backend.graabackend.retrofit

import backend.graabackend.retrofit.endpoints.CollectionControllerTonApiEndpoints
import backend.graabackend.retrofit.endpoints.NftControllerTonApiEndpoints
import backend.graabackend.retrofit.endpoints.SearchControllerTonApiEndpoints
import retrofit2.converter.gson.GsonConverterFactory
import io.github.cdimascio.dotenv.dotenv
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit


@Configuration
class RetrofitConfig {
//    private val dotenv = dotenv {
//        directory = "/app"
//        filename = ".env"
//    }
//
//    val baseUrl = dotenv["TON_API_BASE_URL"]
    val baseUrl = "https://tonapi.io"

//    val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor("your-hardcoded-token"))
//        .build()

    fun buildNftRetrofitObject(): NftControllerTonApiEndpoints = Retrofit.Builder()
        .baseUrl(baseUrl)
//           .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NftControllerTonApiEndpoints::class.java)

    fun buildCollectionRetrofitObject(): CollectionControllerTonApiEndpoints = Retrofit.Builder()
        .baseUrl(baseUrl)
//           .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CollectionControllerTonApiEndpoints::class.java)


    fun buildSearchRetrofitObject(): SearchControllerTonApiEndpoints = Retrofit.Builder()
        .baseUrl(baseUrl)
//           .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchControllerTonApiEndpoints::class.java)

}

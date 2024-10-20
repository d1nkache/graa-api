package backend.graabackend.retrofit

import backend.graabackend.retrofit.endpoints.*
import retrofit2.converter.gson.GsonConverterFactory
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


@Configuration
class RetrofitConfig {
//    private val dotenv = dotenv {
//        directory = "/app"
//        filename = ".env"
//    }
//
//    val baseUrl = dotenv["TON_API_BASE_URL"]
    val tonApiBaseUrl = "https://tonapi.io"
    val getGemsApiBaseUrl = "https://api.getgems.io"
    val okHttpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
//    val client = OkHttpClient.Builder()
//        .connectTimeout(100, TimeUnit.SECONDS)  // Время на установку соединения
//        .readTimeout(100, TimeUnit.SECONDS)     // Время на чтение данных от сервера
//        .writeTimeout(100, TimeUnit.SECONDS)    // Время на отправку данных на сервер
//        .build()

    fun buildNftGetGemsRetrofitObject(): NftControllerGraphqlEndpoints = Retrofit.Builder()
        .baseUrl(getGemsApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NftControllerGraphqlEndpoints::class.java)

    fun buildNftRetrofitObject(): NftControllerTonApiEndpoints = Retrofit.Builder()
        .baseUrl(tonApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
        .build()
        .create(NftControllerTonApiEndpoints::class.java)

    fun buildCollectionRetrofitObject(): CollectionControllerTonApiEndpoints = Retrofit.Builder()
        .baseUrl(tonApiBaseUrl)
//        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CollectionControllerTonApiEndpoints::class.java)


    fun buildSearchRetrofitObject(): SearchControllerTonApiEndpoints = Retrofit.Builder()
        .baseUrl(tonApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchControllerTonApiEndpoints::class.java)

    fun buildUserRetrofitObject(): UserControllerTonApiEndpoints = Retrofit.Builder()
        .baseUrl(tonApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserControllerTonApiEndpoints::class.java)

    fun buildUserGetGemsRetrofitObject(): UserControllerGraphqlEndpoints = Retrofit.Builder()
        .baseUrl(getGemsApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserControllerGraphqlEndpoints::class.java)

    fun buildFillDatabaseRetrofitObject(): FillDatabaseTonApiEndpoints = Retrofit.Builder()
        .baseUrl(tonApiBaseUrl)
//        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FillDatabaseTonApiEndpoints::class.java)
}

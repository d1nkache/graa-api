package backend.graabackend.retrofit

import backend.graabackend.retrofit.endpoints.SearchTonApiEndpoints
import retrofit2.converter.gson.GsonConverterFactory
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit


@Configuration
class RetrofitConfig {
    private val dotenv = dotenv {
        directory = "/app"
        filename = ".env"
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor("your-hardcoded-token"))
        .build()

    fun buildRetrofitObject(): SearchTonApiEndpoints {
        val baseUrl = dotenv["TON_API_BASE_URL"]
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchTonApiEndpoints::class.java)

        return retrofit
    }
}

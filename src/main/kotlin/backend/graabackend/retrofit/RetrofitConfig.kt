package backend.graabackend.retrofit

import backend.graabackend.retrofit.endpoints.SearchTonApiEndpoints
import retrofit2.converter.gson.GsonConverterFactory
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit

@Configuration
class RetrofitConfig {
    fun buildRetrofitObject(): SearchTonApiEndpoints {
        val baseUrl = Dotenv.load()["TON_API_BASE_URL"]
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchTonApiEndpoints::class.java)

        return retrofit
    }
}

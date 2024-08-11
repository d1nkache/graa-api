package backend.graabackend.service.impl

import backend.graabackend.model.response.SearchCollectionResponse
import backend.graabackend.retrofit.endpoints.SearchTonApiEndpoints
import backend.graabackend.service.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Service
class SearchServiceImpl : SearchService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val searchTonApiService = retrofit.create(SearchTonApiEndpoints::class.java)

    override fun globalSearchCollection(collectionAddress: String, description: String): SearchCollectionResponse {

    }


    override fun globalSearchNft(nftAddress: String, description: String): Any {
        TODO("Not yet implemented")
    }

    override fun globalSearchAccount(domain: String): Any {
        TODO("Not yet implemented")
    }

    override fun localSearchNft(nftAddress: String, description: String): Any {
        TODO("Not yet implemented")
    }
}
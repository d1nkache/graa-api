package backend.graabackend.controller

import backend.graabackend.model.request.SearchRequest

interface SearchController {
    fun globalSearchCollection(request: SearchRequest): Any
    fun globalSearchNft(request: SearchRequest): Any
    fun globalSearchAccount(request: SearchRequest): Any
    fun localSearchNft(request: SearchRequest): Any
}


package br.com.alissonfpmorais.pokedex.network

import br.com.alissonfpmorais.pokedex.models.wrappers.PokemonCatalog
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by STwis on 27/11/2017.
 */
interface PokeService {
    companion object {
        val BASE_URL = "http://pokeapi.co/api/v2/"
        val POKEMON_BATCH_LOAD = 20
        val POKEMON_OFFSET_LOAD = 0
        val POKEMON_IMAGE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
        val POKEMON_IMAGE_FORMAT = ".png"

        fun getRetrofit(baseURL: String) : Retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(baseURL)
                .build()
    }

    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int) : Call<PokemonCatalog>
}
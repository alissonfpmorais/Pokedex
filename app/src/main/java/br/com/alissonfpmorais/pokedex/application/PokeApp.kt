package br.com.alissonfpmorais.pokedex.application

import android.app.Application
import android.content.Context
import br.com.alissonfpmorais.pokedex.network.PokeService
import br.com.alissonfpmorais.pokedex.views.PokemonListAdapter
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.autoAndroidModule
import retrofit2.Retrofit

/**
 * Created by STwis on 27/11/2017.
 */
class PokeApp : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        import(autoAndroidModule(this@PokeApp))
        bind<Retrofit>() with multiton { baseURL: String -> PokeService.getRetrofit(baseURL)}
        bind<PokemonListAdapter>() with factory { context: Context -> PokemonListAdapter(context) }
    }
}
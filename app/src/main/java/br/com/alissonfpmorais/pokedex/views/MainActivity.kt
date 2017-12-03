package br.com.alissonfpmorais.pokedex.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import br.com.alissonfpmorais.pokedex.R
import br.com.alissonfpmorais.pokedex.models.wrappers.PokemonCatalog
import br.com.alissonfpmorais.pokedex.network.PokeService
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : KodeinAppCompatActivity() {
    var offset = PokeService.POKEMON_OFFSET_LOAD
    var isReadyToLoad = false

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pokemonListAdapter: PokemonListAdapter by with(this.applicationContext).instance()
        val layoutManager = GridLayoutManager(this, 3)

        pokemonList.adapter = pokemonListAdapter
        pokemonList.setHasFixedSize(true)
        pokemonList.layoutManager = layoutManager
        pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (isReadyToLoad) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.d(this.javaClass.name, "Scrolled to the end")
                            pokemonProgressLoad.visibility = View.VISIBLE
                            isReadyToLoad = false
                            offset += PokeService.POKEMON_BATCH_LOAD
                            loadData(pokemonListAdapter)
                        }
                    }
                }
            }
        })

        loadData(pokemonListAdapter)
    }

    fun loadData(pokemonListAdapter : PokemonListAdapter) {
        val retrofit: Retrofit by with(PokeService.BASE_URL).instance()
        val service = retrofit.create(PokeService::class.java)
        val callable = service.getPokemonList(PokeService.POKEMON_BATCH_LOAD, offset)

        callable.enqueue(object : Callback<PokemonCatalog> {
            override fun onResponse(call: Call<PokemonCatalog>?, response: Response<PokemonCatalog>?) {
                pokemonProgressLoad.visibility = View.GONE

                Log.d(this.javaClass.name, "Response received!")

                if (response != null) {
                    when {
                        response.isSuccessful -> {
                            Log.d(this.javaClass.name, "Response success!")
                            pokemonListAdapter.data = response.body()?.results ?: listOf()
                        }
                        else -> Log.d(this.javaClass.name, "Response error: ${response.errorBody()}")
                    }
                }

                isReadyToLoad = true
            }

            override fun onFailure(call: Call<PokemonCatalog>?, t: Throwable?) {
                Log.d(this.javaClass.name, "Error: ${t?.message}")
                isReadyToLoad = true
            }
        })
    }
}

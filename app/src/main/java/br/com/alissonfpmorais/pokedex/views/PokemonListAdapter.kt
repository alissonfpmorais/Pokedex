package br.com.alissonfpmorais.pokedex.views

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.alissonfpmorais.pokedex.R
import br.com.alissonfpmorais.pokedex.models.wrappers.PokemonResult
import br.com.alissonfpmorais.pokedex.network.PokeService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pokemon.*

/**
 * Created by STwis on 29/11/2017.
 */
class PokemonListAdapter(private val context: Context) : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {
    var data : List<PokemonResult> = listOf()
        set(value) {
            field = field.plus(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_pokemon, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val pokemonResult = data[position]
        val pokemonNumber = context.getString(R.string.pokemon_number_format, pokemonResult.number)

        holder?.pokemonName?.text = pokemonResult.name
        holder?.pokemonNumber?.text = pokemonNumber

        val url = PokeService.POKEMON_IMAGE_URL + pokemonResult.number + PokeService.POKEMON_IMAGE_FORMAT

        val options = RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

        val transition = DrawableTransitionOptions()
                .crossFade()

        Glide.with(context)
                .load(url)
                .apply(options)
                .transition(transition)
                .into(holder?.pokemonImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
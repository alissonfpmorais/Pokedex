package br.com.alissonfpmorais.pokedex.models.wrappers

/**
 * Created by STwis on 27/11/2017.
 */
data class PokemonResult(val name: String, val url: String) {
    var number: Int = 0
        get() {
            val splittedURL = url.split("/")
            return Integer.parseInt(splittedURL[splittedURL.size - 2])
        }
}
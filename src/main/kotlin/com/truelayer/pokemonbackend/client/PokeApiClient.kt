package com.truelayer.pokemonbackend.client

import com.truelayer.pokemonbackend.client.model.PokeApiClientPokemonSpecies

/**
 * This interface is a contract that:
 *
 * Given a name of a Pokemon then returns Pokemon species data.
 *
 * The interface was created because:
 * A. it will make possible to unit test PokemonRestController after integration with it
 * B. in the future it could have implementations that query a local cache
 */
interface PokeApiClient {
    fun getPokemonData(name: String): Result<PokeApiClientPokemonSpecies>

}
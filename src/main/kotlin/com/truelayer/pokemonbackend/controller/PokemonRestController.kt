package com.truelayer.pokemonbackend.controller

import com.truelayer.pokemonbackend.client.PokeApiClient
import com.truelayer.pokemonbackend.client.httpclient.JavaHttpClientPokeApiClient
import com.truelayer.pokemonbackend.transport.model.PokemonDescriptionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/pokemon")
class PokemonRestController(@Autowired val pokeApiClient: PokeApiClient) {

    /**
     * name parameter is required, in this case if it's not provided it will return HTTP Code 404
     *
     * A corner case is if name = "translated" the request will be handled by this method.
     */
    @GetMapping("/{name}")
    fun pokemon(@PathVariable name: String): PokemonDescriptionResponse {
        var output = PokemonDescriptionResponse(name = name,
            habitat = null, description = null, legendary = null)
        val data = pokeApiClient.getPokemonData(name)
        data.onSuccess {
            output = PokemonDescriptionResponse(name = it.name, habitat = it.habitatName, description = it.flavorText, legendary = it.legendary)
        }.onFailure {
            if (it is JavaHttpClientPokeApiClient.PokeApiException) {
                if (it.httpStatusCode == 404) {
                    throw ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pokemon not found"
                    );
                }
                throw it
            }
            throw it
        }
        return output
    }

    @GetMapping("/translated/{name}")
    fun translatedPokemon(@PathVariable name: String): PokemonDescriptionResponse {
        return PokemonDescriptionResponse(description = "translated-$name", habitat = null, name = name, legendary = null)
    }
}
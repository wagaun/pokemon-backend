package com.truelayer.pokemonbackend.controller

import com.truelayer.pokemonbackend.transport.model.PokemonDescriptionResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pokemon")
class PokemonRestController {

    /**
     * name parameter is required, in this case if it's not provided it will return HTTP Code 404
     *
     * A corner case is if name = "translated" the request will be handled by this method.
     */
    @GetMapping("/{name}")
    fun pokemon(@PathVariable name: String): PokemonDescriptionResponse {
        return PokemonDescriptionResponse(description = name)
    }

    @GetMapping("/translated/{name}")
    fun translatedPokemon(@PathVariable name: String): PokemonDescriptionResponse {
        return PokemonDescriptionResponse(description = "translated-$name")
    }
}
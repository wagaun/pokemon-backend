package com.truelayer.pokemonbackend.controller

import com.truelayer.pokemonbackend.transport.model.PokemonDescriptionResponse
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class PokemonRestControllerTest {

    private val controller = PokemonRestController()

    @Test
    fun `Test pokemon method`() {
        assertThat(controller.pokemon("pikachu"), equalTo(PokemonDescriptionResponse("pikachu")))
    }

    @Test
    fun `Test translated pokemon method`() {
        assertThat(controller.translatedPokemon("pikachu"), equalTo(PokemonDescriptionResponse("translated-pikachu")))
    }
}
package com.truelayer.pokemonbackend.controller

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class PokemonRestControllerTest {

    private val controller = PokemonRestController()

    @Test
    fun `Test root method`() {
        assertThat(controller.root(), equalTo("Gotta catch em all!"))
    }
}
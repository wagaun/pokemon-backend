package com.truelayer.pokemonbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PokemonRestController {

    @GetMapping("/")
    fun root() =
        "Gotta catch em all!";
}
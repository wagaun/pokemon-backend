package com.truelayer.pokemonbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PokemonBackendApplication

fun main(args: Array<String>) {
	runApplication<PokemonBackendApplication>(*args)
}

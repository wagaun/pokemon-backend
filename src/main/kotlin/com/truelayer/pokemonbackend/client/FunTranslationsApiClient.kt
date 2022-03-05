package com.truelayer.pokemonbackend.client

interface FunTranslationsApiClient {
    fun translate(text: String): Result<Translated>

    data class Translated(val translatedText: String)
}
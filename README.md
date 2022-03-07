# Getting started with pokemon-backend

When it's the first time after you check out the package, run:

    # This command builds the WAR file
    ./gradlew build
    # This command will build the docker image
    docker build -t wagner/pokemon-backend .
    # This command will start the service on port 8080
    docker run -p 8080:8080 -t wagner/pokemon-backend

These commands will start the service locally listening to port 8080.

# Notes on trade-offs and possible next steps

The current implementation doesn't support pipeline stages. In real life it would be nice to have a mechanism like Spring profiles to allow different configurations per stage, for example, devo, beta, gamma and prod.

That would allow the stages to be independent and run in a slightly different manner. For example, in devo or beta we could implement the capacity of mocking dependency calls so that we could write black box integration tests. In gamma we could have a few end-to-end tests to ensure the integration works before prod deployment. While in Prod we could implement prod sanity checks (to make sure things still work as they should).

There are a couple of disabled tests in com.truelayer.pokemonbackend.client.httpclient package to test the dependencies clients implementation. It's disabled because we don't want unit tests that call external endpoints, also we don't want to be throttled by our dependencies.

In production, assuming that:

    A. the number of Pokemons is small (< millions) and the same for translations (2 times number of Pokemons)\
    B. the metadata doesn't change often

We could consider:

    - have local read databases and a process to update them
    - implement a cache system
    - keep in memory structures with the required data and an update process

Each option would have pros and cons.

If it was production code we could have a docker-compose file to start a nginx to map pockemon-backend service to default http(s) ports 80/443. That would make it easier to test a frontend locally, if there is one.

One possible improvement is to work on gradle tasks to build the docker image and run it.

The PokeAPI and FunTranslationsAPI could be more robust handling better HTTP error codes. I'm not sure how that will behave if there is a redirection, for example.
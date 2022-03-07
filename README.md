# Getting started with pokemon-backend

When it's the first time after you check out the package, run:

    # This command builds the WAR file
    ./gradlew build
    # This command will build the docker image
    docker build -t wagner/pokemon-backend .
    # This command will start the service on port 8080
    docker run -p 8080:8080 -t wagner/pokemon-backend

These commands will start the service locally listening to port 8080.


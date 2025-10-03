# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEAO4AFkhghgBKKPZIqhZySBBogaHhYoiopAC0AHzklDRQAFwwANoACgDyZAAqALowAPQ+BlAAOmgA3gBErZRowAC2KD3FPTA9ADSTuOpB0BxjE9OTKEPASAjLkwC+psJFMHms7FyUpb39UIMjO6s9c6oLUEvjkzM965vb7z37mDYnG4sBOh1EpSgMTiESgAAporF4pRogBHHxqMAASkw4JUx3ysnkShU6lK9hQYAAqm04ddbigcUTFMo1KoCUYdMUAGJITgwGmUZkwHSWGD04ZiTDMkls45gwoQmBoHwIBC4xX4k4y1lkkBQuQoQXw67MpnaWXqDnGYoKDgcAVtZkakRawkW3WqYr6lCGhQ+MAhOHAAMhM3Sj2k9knG12h3+wPOvGGbInIHnEowREwlFqVVYdMg+X5Q4XGBXNoMialFafEOB2oQADW6Grkz2LqOqfymXMpQATE4nJ0KwNJW3a5N6yFGy20BOO+gOKYvL5-AFoOwKTAADIQWIpYJhCKYXs5EuFMuVGoNZoGdRJNAjiUjGZPF4cfal0H5QtlrqTC+ozjA876LMsEz7H+P4FK6KClAg+58nCe4HmiGLxDiyYcjqUbkpSxp0pWkrmsSnrWlyvL8sawqiuKxEjBGZFRsWsFUEqNHaJ2og4ZGbKlHCACSABmjpCtoMBga8MAALwyZJ+jPIsWKdD6fqhsGobhrhcoxlycAGhEMAJmGXHYd2pzAmWqF8tEqj5oCZxFhZ36XIBDHAT0DzTrOrYgTAX6XqCJxnmAA5DiOfQeQu3mhr584wDWi6cCu3h+IEXgoOgu77r4zBHukp7IOYHKueU0gAKI7hVtQVY0TT3qoj6dD5zboJ2lActBpTTtASAAF6JMkpQADytXOuSOVZMHJghuUBih81gOhmJYZqKbusx-EcCg3ARCZmkNm1aCkSyLF6cU0i7ZShgmbRYrje1OlWi562lHdZnrax3WoXldkOdBrFlV0gXUJ1FmheFw7dPsS6pWugRQg6O4wjAADikrsgVJ6haVQWlGUaM1fV9iSi1cXHR1MHdTAvVQANQ1oKNj1oJNgMKnBCEwhjIyqCh3OYytmHcW6Mh8WSFJgAdLOnZa0b5DaVHxlpEl0SzTFnbpHPsSo70q-IIsbWLW1ksg8Q82ocKy+RF1K8Zaro5jGty0Db0wBVRRioJaDxOw+IQKJFtJl9FndRbKPxFNGau2DmYAT0pO82M5S9InKCCdIycAIz9gAzAALJ8aQRMaVYgZ8OgIKATal+O5eTGnABykogbsMD1KDXYhcVGBQyODeY8nZSp5KGfZ3nheTMXRrRfXPSV9Xtd3HPTct15bcd2YKWeGl64bBSGDAPAhmGBbqTHhkPfMNrV7VHUJNk4MFNziOq8jJ34O-k5ZZ0wzBDDTAMaz90C5H7gnSUzdl4rAClHZyF5OYwDUhEC2cIDK+mQZKIW2JDa8RNl6SW0tgEnWdjbBWlE+TK0TKrB6RCSHnW1kqD6BtzKbU1nqE+KC07aXFvLTktp7SOxGMHOC31v6ZjQYaC2-0EAFjETHIoblwEjDHqUHOBcYHflYpDGAg5obxzTiomAaj84wLhjvBGARLC7UQkEGAAApCAfJBGGACAvEATYipZGvvAhR5QKhUlvE0NO5Mjov26NgKuVioBwAgIhKAMwDHSA-tTMRPVQx9UGv-JmgCWagO6LMSJlAYlxOWAAdRYIJWqTQABCO4FBwAANIPESePdRUE5EMN1jAAAVo4tAKCHF8ikSgdEq0cHah4cUAhGkZZ0N0mQnkFDjL6yPmrWhz1oydPgssqhzCQ6sLlvhMAnDR7SGtudBZdsg7UJgIkuZL0tmlGuXskREy8HFCEqJCJwAonFOgLJeS5TKkVRUiqbAWgIjTyXigIiY4RjnPmXwqk4LDQwChR5NF4QQj0ThWIFhxs2FejhBVBABhQV+AhSgNxNcPKwpuCRe5vCbTIspSKKu7joWYsDDi+ljF8UbOKBSyRkoraMoosUFlhp2RpxgPEemDhxlplSfYvpwz7IyNgeDG+cdklaKvn3GGW9lzmPSgELwPz4DcDwLTbAETCCM3PoVXG2qCaVWqrVeqxgqZdVSSDTVM03ogCtfCNaryDmem9MG0VGzxWIF9LAZkegDCKq-tNUoQb43SNkdNeRFw-WaIhvq3RI5YYpSAA
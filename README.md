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
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5T9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFMAAyENFobJzcZTD9g1AjYxNTs33zqotQy29rfQ2Wx2-wOFyuNy4lBOhUi0VilCOonKUCiMUyUAAFHD0ZRIgBHHxqMAASkwOIRUCRKhhMAAqh1pAoauV7CgwAzKJini8yZyoEyarTjOUAGJITj0xnaGA6MI84CjTD8wUnU4UjHUlDlNA+BAIcloyla2kq5nlECouQofnc6XyPmM5nCnTlBQcDhSyiyeSG+Ga4qiWkKHxgVKCi1WzIhsOY4Ch8PaMkx8PO04imDuz0pn3AP24qmBmm5dVGjGQu7lDV4tR6rDVqAV6ElwpHSjlR4dF6TcqrD7xsP1CAAa3QPam+0OxWbp2y5nKACYnE5up2hoqxjBe+8pgPUkPR2hx6sDugOJ5vH5AtB2GzzpcJSl0plMHO8q3p-dqnUmq0DOoEjQVcBi7DddgBL4fj+E8p2oZtCnBCUm3uNdnjA-4PkgpZwLBB9rnYKFYFORC0C1coEDwzESIJIlYjJEiTVOM0WRgNkOQ6O111GR1vTTQoM3FSUVRlOUYAVJVmLVBC8LIr0BW0TAGKLQxThzc0YEtFBrRTOME1zZM9L4oxXUzD1Mz0hSlJEYtiLw5Dymo2sECwEjkKkoo4JQvo9wPMctwnPoDjbIjZ2QecYCXFdem8hNfKPfyYLPC9fH8AIvBQdB73sXxmACNIMiyML3w8kpygqaQAFEzgq+oKuaFp-1UQDuh8kd0Fg45bIhAjKxgVrD0Uuyevg8ywwjIwUG4aME10wc2rQAyxqMjNImGCAaFGxNfVc4aQo-TyHMuHLIlUOsOpnQo3zARdl2A0Z1GANlj3BNlPRywKzE4ZKrwCVFPTOdEYAAcQ3VQnwK18iuYU5grKoGavq+wNxa2L5vOojCgAKQgCUQdGey+tRgbsdxjc3NUwyWKoYBkC0abY369BFtTIV0xMwTsws+RZTCRm0EwNTWdOEm0DxlACZzBSRbF8nCjF8bkFiMXVExMl5eW9mJWzfVgdBzB1aFrGcdFsndvKMWAdiTBpdN24LpK9tddGS2sGC9yrpuqKLfRT7z0wLwUsCbAfCgbBuHgKNDDF8GXyu2lYcqWoGkR5HgiJ9BgKR0YADl0Jgt3heNmWzYeGK5sPcCPizlBc9GHDraL23CODSnymp2ntJmvnmdVNmxS1zbcx5wny-awX3Jt-GS8l31J-F3baQNyMtMyMXMWr-T9Y3Xv+JMrMnZQXMG9Jqe7b2h37jFuAo0Ak6zoL0KcmuiLbui6uAElpGPSckv9y9UssJNCiyQYAiwPoEHQCBQDDkhk-eOn4ypVDpL+Fo1cUajyAtFMOwBAFQDgBACiUBK5TA-l-UE6NaRzwJv0PmxDPhQNwfgwhdDSH1yoQvCmS0qY0yQHTFAOlu4C0pi6fukoZ7AGHnzIRXCJ6N1PoRN0XM8zsLPovbe6kABWciUDrw3J-WY2DGEEOgGrdRrNd7lDpNgPhB8j4qObi2C+5sNwizvs5ChjiPYvyiv0e6qhHqbl7CLFAnpq4fV-gHH6XgcHwG4HgPq2Aw6EHiIkGOhU4EwwQZUSq1Var1WMB4woBMQBxPkvIPQBhVaYFljIbQSgVDqAtKUhQ+pcxVNzPUtQYM+4yEmuyQwLSEC1N9OTTJB1YlaSgG412n53ZQ09ndLpATjy5gqYYEpkyQnhK+kAA

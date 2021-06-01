# BOX GAME

- A game implemented in [OpenJFX](https://openjfx.io/) based on the Model-View-Controller (MVC) pattern.
- The project uses [JUnit5](https://junit.org/junit5/) and game results are stored in a [JSON](https://www.json.org/json-en.html) file with [FasterXML Jackson](https://github.com/FasterXML/jackson/).



## Game rules
- There are 16 small boxes arranged in a line, and 3 red stones and 3 black stones.


- In the beginning the stones are arranged as follows:

      🔴 ⚫️ 🔴 ⚫ 🔴 ⚫

- In a move, take out two adjacent stones, then put into two adjacent empty boxes.


- Arrange the order of stones as follows win the game:

      🔴 🔴 🔴 ⚫️ ⚫ ⚫



## Requirements
- Building the project requires JDK 16 and [Apache Maven](https://maven.apache.org/)

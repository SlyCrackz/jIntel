# jIntel

## Introduction
jIntel is a versatile tool designed for EVE Online enthusiasts. It leverages the zKillboard API to provide in-depth statistics for characters and corporations in the game, featuring a user-friendly interface with FlatLaf styling.

## Features
- Retrieve and display detailed character statistics from zKillboard.
- Access corporation data based on character affiliations.
- User-friendly interface using FlatLaf for an enhanced experience.

## Getting Started

### Prerequisites
- Java JDK 11 or newer.
- IntelliJ IDEA with Gradle support.

### Setup and Installation
1. Clone the jIntel repository:
2. Open the project in IntelliJ IDEA. IntelliJ should automatically handle Gradle dependencies and setup.

## Building the Application
With IntelliJ IDEA, building the project is straightforward:
1. Open the project in IntelliJ IDEA.
2. IntelliJ will automatically handle the Gradle build process. Wait for the process to complete.
3. To build an executable jar, run the `shadowJar` task from the Gradle panel.

## Usage
Run jIntel by executing the generated jar file after building:
java -jar jIntel-fat.jar or by just double clicking the jar.
(Adjust the jar file name based on the version you built.)

### How to Use jIntel
- Input a character name in the text field provided in the application.
- Hit the "Enter" or "Return" key to fetch and display relevant statistics from zKillboard for the character and their affiliated corporation.

## Contributing
We welcome contributions to jIntel. Feel free to fork the repository, make your changes, and submit a pull request for review.

## Acknowledgments
Special thanks to EVE Online and zKillboard for providing the APIs that power this project.

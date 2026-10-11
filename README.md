# Blastnite

A 2D side-scrolling shooter platformer in Java Swing. A / D to run, Space to jump, J to shoot, R to restart after winning or losing.

## Requirements
Java 17 or newer (JDK). No external libraries.

## Run in an IDE (easiest)
Open the project folder in NetBeans, IntelliJ or VS Code. It is a Maven project, so the IDE
recognises it from `pom.xml`. Run `blastnite.Main`.

## Build & run with Maven
    mvn package
    java -jar target/blastnite-1.0-SNAPSHOT.jar

## Build & run without Maven (plain JDK)
macOS / Linux:
    javac -d out $(find src/main/java -name "*.java")
    java -cp out:src/main/resources blastnite.Main

Windows (PowerShell):
    javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
    java -cp "out;src/main/resources" blastnite.Main

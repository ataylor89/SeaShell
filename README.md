# SeaShell

## Requirements
Sea Shell requires
1. Java version 17
2. Maven

Both Java 17 and Maven can be downloaded on Mac OS X from the official websites or from a package manager like homebrew

## Installation
Sea Shell can be installed on Mac OS X using the shell script macbuild.sh

In the SeaShell folder type

    % chmod +x macbuild.sh
    % ./macbuild.sh 

This should install the icons, the JAR files, and the Mac OS X application

## Configuration
SeaShell has a configuration file .seashell 

This file becomes active once it is installed in your home directory, ~/.seashell

SeaShell loads ~/.seashell every time a new tab is created and gets its default configuration from ~/.seashell

## macbuild.sh
The macbuild.sh script does the following
1. Copies .seashell to home directory, creating the file ~/.seashell 
2. Creates an icons.iconset directory in the SeaShell folder
3. Creates icons in the icons.iconset folder using <code>sips</code>
4. Converts the icons.iconset folder to an Apple Icon Image (icns file) using <code>iconutil</code>
5. Cleans the source and output directories, compiles the Java code, packages bytecode into JARs, using <code>mvn clean install</code>
6. Assembles an all-in-one jar using the <code>mvn assembly:single</code> Maven plugin
7. Creates an Apple Disk Image (dmg file) using the jpackage tool that comes with Java 17

## Object model 
1. There is one instance of the SeaShell JFrame 
2. There are multiple instances of SeaShellTab, one for every tab that is created 
3. Every tab has an Interpreter for interpreting commands and a Config for loading settings 
4. There is one instance of Logger, which is created in AppLogger.java 
5. The Interpreter stores a reference to a running process and creates new processes as needed 
6. The Config loads the path variable, the color scheme, and the prefix string 
7. The Logger has a StreamHandler and a FileHandler, for logging to stdout and to file 
8. The Path class parses a path or a parent-child pair of paths and returns a file
9. The SeaShell instance listens for many action events and handles them
10. Every SeaShellTab instance listens for many key events and key strokes and handles them

## Key events and key strokes
1. Pressing the return key ("enter") in a tab runs a command
2. ctrl+d and ctrl+c are keyboard interrupts that close a running process

## Text substitutions
1. The ~ character gets replaced with the home directory
2. The .. sequence gets replaced with the parent directory

## Path
The default PATH is "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin" which is set in Config.java

The PATH and other properties can be modified in the properties file ~/.seashell

## Maven
Maven is a Java build tool that can compile Java, package bytecode into JARs, assemble a project into a single JAR, download dependencies at compiletime from the Maven central repository, and generate javadoc documentation for a project

## pom.xml
The Project Object Model (POM) is the basic concept of Maven

The pom.xml file has plugins that help Maven assemble the source code into a single JAR (SeaShell-jar-with-dependencies.jar)
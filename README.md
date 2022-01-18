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
4. Converts the icons.iconset folder to an .icns file using <code>iconutil</code>
5. Cleans the source and output directories, compiles the Java code, packages bytecode into JARs, using <code>mvn clean install</code>
6. Assembles an all-in-one jar using the <code>mvn assembly:single</code> Maven plugin
7. Creates an Apple Disk Image (dmg file) using the jpackage tool that comes with Java 17
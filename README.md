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

## Configuration file .seashell
If you would like to install the configuration file .seashell in your home directory, you can uncomment the first three lines of the macbuild.sh shell script 

You can modify ~/.seashell to set the default configuration for SeaShell

The configuration file ~/.seashell lets you set the color theme, the path variable, and the prefix variable
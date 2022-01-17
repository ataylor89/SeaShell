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
SeaShell has a configuration file ~/.seashell which can be installed in your home directory in the macbuild.sh shell script

The lines that install ~/.seashell are commented out You can uncomment these lines to make it a part of the build script

You can modify the ~/.seashell configuration file to set the path variable, the color scheme, and the prefix variable
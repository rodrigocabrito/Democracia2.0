#!/bin/bash

# Set the Java home directory
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

mvn clean install
mvn javafx:run

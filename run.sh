#!/bin/bash

export CLASSPATH=./src/:./lib/bcel-5.2.jar
javac -d ./bin src/Scratch.java src/Parse.java
export CLASSPATH=./bin/:./lib/bcel-5.2.jar
echo "Before instrumentation..."
java Scratch
java Parse
export CLASSPATH=./instrumented/:./lib/bcel-5.2.jar
echo "After instrumentation..."
java Scratch
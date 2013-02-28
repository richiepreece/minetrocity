#!/bin/bash
find . -name "*.java" -print > sources.txt
javac -source 1.3 -target 1.1 -classpath "../lib/*" -d "../build/debug/classes" -sourcepath "./3DzzD/src;./3DzzDExtensionJOGL/src;./Extension/src" @sources.txt
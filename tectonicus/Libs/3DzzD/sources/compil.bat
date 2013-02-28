dir /b /s *.java > sources.txt
javac -source 1.3 -target 1.1 -classpath "../lib/jogl.jar;../lib/gluegen-rt.jar;../lib/timer.jar;../lib/netscape.jar" -d "../build/debug/classes" -sourcepath "./3DzzD/src;./3DzzDExtensionJOGL/src;./Extension/src" @sources.txt
pause
'UNIX compil
'find . "*.java" -print > sources.txt
'javac -source 1.3 -target 1.1 -classpath "../lib/jogl.jar;../lib/gluegen-rt.jar;../lib/timer.jar;../lib/netscape.jar" -d "../build/debug/classes" -sourcepath "./3DzzD/src;./3DzzDExtensionJOGL/src;./Extension/src" @sources.txt
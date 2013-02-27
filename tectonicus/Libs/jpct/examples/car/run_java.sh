#!/bin/bash

java -Djava.library.path=../../lib/lwjgl-2.4.2/native/linux -cp ../../lib/lwjgl-2.4.2/jar/lwjgl.jar:../../lib/lwjgl-2.4.2/jar/lwjgl_util.jar:../../lib/jpct/jpct.jar:car.jar -Xmx128m CarTest width=640 height=480 refresh=0 mipmap


#!/bin/sh

mvn install:install-file -Dfile=WebSocket.jar -DgroupId=org.java_websocket -DartifactId=websocket -Dversion=0.1 -Dpackaging=jar
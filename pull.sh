#!/bin/sh

git pull
mvn package -Dmaven.test.skip=true
cp crawl-server/target/crawl-server-1.0-SNAPSHOT.jar .

#!/bin/sh

cd gateway; ./gradlew clean build; cd ..
cd resource; ./gradlew clean build; cd ..
cd ui; ./gradlew clean build; cd ..

#!/usr/bin/env bash

rm -r ./betas
mkdir ./betas

for p in fabric neoforge; do
  rm ./$p/build/libs/*
  ./gradlew $p:build
  rm ./$p/build/libs/*-shadow.jar
  rm ./$p/build/libs/*-sources.jar
  mv ./$p/build/libs/*.jar ./betas
done
#!/usr/bin/env bash

javac ../client/*.java
jar cfm ../client.jar myManifest.txt ../client/*.class

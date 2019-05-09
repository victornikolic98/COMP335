#!/usr/bin/env bash

javac ../client/*.java
jar cfm ../myClient.jar myManifest.txt ../client/*.class

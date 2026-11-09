#!/bin/bash
# Compila y ejecuta ContaGest (Linux, Mac, GitHub Codespaces).
# Uso:  ./ejecutar.sh          -> menu interactivo
#       ./ejecutar.sh --demo   -> demostracion automatica del CRUD
cd "$(dirname "$0")" || exit 1
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java") || exit 1
cp src/main/resources/* out/
java -cp "out:lib/h2-2.2.220.jar" com.contagest.Main "$@"

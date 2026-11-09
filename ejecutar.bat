@echo off
rem Compila y ejecuta ContaGest en Windows.
rem Uso:  ejecutar.bat          -> menu interactivo
rem       ejecutar.bat --demo   -> demostracion automatica del CRUD
cd /d "%~dp0"
if not exist out mkdir out
dir /s /b src\main\java\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
if errorlevel 1 exit /b 1
copy /y src\main\resources\* out\ > nul
java -cp "out;lib\h2-2.2.220.jar" com.contagest.Main %*

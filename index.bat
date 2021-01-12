@echo off
cls
if exist build rmdir /s /q build
mkdir build
cd src
javac -cp com/krzem/test_3d_game/modules/jogl-all.jar;com/krzem/test_3d_game/modules/jogl-all-natives-windows-i586.jar;com/krzem/test_3d_game/modules/gluegen-rt.jar;com/krzem/test_3d_game/modules/gluegen-rt-natives-windows-i586.jar; -d ../build com/krzem/test_3d_game/Main.java&&jar cvmf ../manifest.mf ../build/test_3d_game.jar -C ../build *&&goto run
cd ..
goto end
:run
cd ..
pushd "build"
for /D %%D in ("*") do (
	rd /S /Q "%%~D"
)
for %%F in ("*") do (
	if /I not "%%~nxF"=="test_3d_game.jar" del "%%~F"
)
popd
cls
java -jar build/test_3d_game.jar
:end

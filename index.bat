echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac -cp com/krzem/test_3d_game/modules/jogl-all.jar;com/krzem/test_3d_game/modules/jogl-all-natives-windows-i586.jar;com/krzem/test_3d_game/modules/gluegen-rt.jar;com/krzem/test_3d_game/modules/gluegen-rt-natives-windows-i586.jar; com/krzem/test_3d_game/Main.java&&java -cp com/krzem/test_3d_game/modules/jogl-all.jar;com/krzem/test_3d_game/modules/jogl-all-natives-windows-i586.jar;com/krzem/test_3d_game/modules/gluegen-rt.jar;com/krzem/test_3d_game/modules/gluegen-rt-natives-windows-i586.jar; com/krzem/test_3d_game/Main
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"
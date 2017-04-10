@rem D:\workspace2\tvb\deploy>mvn assembly:assembly
@rem mvn  -Du="%HOMEDRIVE%%HOMEPATH%\antx.properties" clean package
@echo off

:INPUT
echo  1.执行clean命令
echo  2.执行eclipse命令
echo  3.执行install命令
echo  4.执行package命令
echo  5.执行下载source文件命令
echo  6.执行tree命令,查看包依赖关系
echo  7.执行deploy命令,打包
echo  q.退出

echo 请输入要选择的命令：
set /p cmd=
IF %cmd% == 1 cls&GOTO CLEAN
IF %cmd% == 2 GOTO ECLIPSE
IF %cmd% == 3 cls&GOTO INSTALL
IF %cmd% == 4 cls&GOTO PACKAGE
IF %cmd% == 5 cls&GOTO SOURCES
IF %cmd% == 6 cls&GOTO TREE
IF %cmd% == 7 cls&GOTO DEPLOY
IF %cmd% == q GOTO END

:CLEAN
call mvn clean eclipse:clean
echo ===============================================================================
echo =========================== clean is complete===============================
echo ===============================================================================
GOTO INPUT

:ECLIPSE
call mvn  eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=false
echo ===============================================================================
echo ===========================-U eclipseis complete===============================
echo ===============================================================================
GOTO INPUT

:INSTALL
call mvn clean install -Dmaven.test.skip=true
@rem copy /y aling-web\target\aling\WEB-INF\logback.xml aling-web\src\main\webapp\WEB-INF
@rem copy /y aling-web\target\aling\WEB-INF\common\uris.xml aling-web\src\main\webapp\WEB-INF\common
@rem call mvn clean install assembly:assembly -Dmaven.test.skip=true -X -e
echo ===============================================================================
echo =========================== install is complete=============================
echo ===============================================================================
GOTO INPUT

:PACKAGE
call mvn package -Dmaven.test.skip=true
echo ============================not clean===================================================
echo =========================== package is complete=============================
echo ===============================================================================
GOTO INPUT

:SOURCES
call mvn dependency:sources
echo ===============================================================================
echo =========================== sources is complete=============================
echo ===============================================================================
GOTO INPUT

:TREE
call mvn dependency:tree
echo ===============================================================================
echo =========================== tree is complete=============================
echo ===============================================================================
GOTO INPUT

:DEPLOY
call mvn deploy -Dmaven.test.skip=true
echo ===============================================================================
echo =========================== deploy is complete=============================
echo ===============================================================================
GOTO INPUT

:END
exit
@pause


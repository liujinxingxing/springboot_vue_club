@echo off
echo running service
echo --------------------------------------------------------------------------
echo 
echo
echo 
echo 
echo
echo --------------------------------------------------------------------------
echo  关闭原进程
for /f "tokens=5" %%i in ('netstat -aon ^| findstr ":38080"') do (
    set n=%%i
)
taskkill /f /pid %n%
echo --------------------------------------------------------------------------
echo 启动 服务
start javaw -Djava.awt.headless=true -Dfile.encoding=UTF-8 -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xms1024m -Xmx1024m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -Dspring.config.location=.\config\application.yml -jar ./club.jar
echo --------------------------------------------------------------------------
exit
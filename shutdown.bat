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
exit
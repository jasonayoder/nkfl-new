@ECHO OFF
:incorrectFile
CD 
ECHO Please point me to the experiment(s): 
set /p target=Enter Directory/File 
:unknown
ECHO The target experiment(s) are %Target%, is this correct? 
set /p confirm=Enter  (y/n) 
IF /I "%confirm%"=="y" goto begin
ECHO Unknown Response
IF /I "%confirm%"=="n" goto incorrectFile
goto unknown
:begin
IF EXIST "%target%\\"  goto directory
IF EXIST "%target%" goto file
ECHO Neither a file nor a directory
goto incorectFile
:directory
ECHO this is a directory, running on all files
@REM CD %target%
FOR /R %%f in (%target%\*.properties) do START "%%f" /B "Java" "-jar" "experimentRunner.jar" "%%f"
PAUSE
EXIT
:file
ECHO this is a file
PAUSE
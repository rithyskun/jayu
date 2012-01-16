@echo OFF  
REM we don't want to see source code of our program, so echo off. @ suppresses any echo for the above line as well.

set BIN_DIR=%~dp0

@echo ON
java -cp "%BIN_DIR%*;." jayu.CsvUtil %*
@echo OFF

@pause
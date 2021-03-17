call runcrud.but
if "%ERRORLEVEL%" == "o"  goto browser
echo.
echo There is problem with runcrud.bat
goto fail

:browser
explorer "http://localhost:8080/crud/v1/task/getTasks"
goto end

:fail
echo.
echo There where errors.

:end
echo.
echo Results:).

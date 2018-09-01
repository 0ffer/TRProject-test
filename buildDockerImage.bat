@echo off

for %%i in (eureka-server user-service zuul) do (
     docker build -t "trproject-test/%%i:latest" %%i
)
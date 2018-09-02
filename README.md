# TRProject-test

##### Сборка и запуск

Для сборки и запуска проекта требуется выполнить следубщие команды из корня проекта:

    bash gradlew clean build -x test
    bash buildDockerImage.sh
    docker-compose up -d 
    
    
##### Точки доступа

Eureka - http://localhost:8761

Документация (с возможностью выполнения запросов) - http://localhost:8060/docs/swagger-ui.html

Непосредственно точки доступа к ролям и пользователям:
- http://localhost:8060/users/**
- http://localhost:8060/roles/**

##### Известные проблемы

- Не работает удаление сущностей
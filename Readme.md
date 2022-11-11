[![Java CI with Maven](https://github.com/evgenkolesman/SimpleChat/actions/workflows/maven.yml/badge.svg)](https://github.com/evgenkolesman/SimpleChat/actions/workflows/maven.yml)

OpenApi доки при запуске приложения тут: http://localhost:8080/swagger-ui/index.html

Авторизацию реализовал чисто символическую, так как для это го нужен фактически еще сервис,
у нас при входе отпраляется POST запрос на /api/v1/auth если все ок то через kafka передается "токен",
конечно лучше было бы реализовать через OAuth 2 – но пока не хватило времени.

В остальном просто REST приложение с 3 мя сервисами: Авторизация, Работа с сообщениями, Работа с пользователями.

Тестирование осуществляется с помощью TestContainers и RestAssured, докер compose можно развернуть с помощью [docker-compose up]
там хранятся контейнеры для локальной работы, возможно добавлю еще имадж самого приложения и будет все вместе подниматься

Чтобы запустить приложение необходимо его скопировать: [git clone https://github.com/evgenkolesman/SimpleChat]
запустить инвайрмент вместе с приложением [docker-compose up] 
далее можно сделать [mvn spring-boot:run] или [mvn org.springframework.boot:spring-boot-maven-plugin:run]
если проблемы с мавеном [java -jar target/simplechat-0.0.1-SNAPSHOT.jar]

ну или ручками в идее запустить

PS если есть проблемы с портами пример:
fuser -vn tcp 5432 получаем номер процесса например 11111
kill 11111 - убиваем процесс

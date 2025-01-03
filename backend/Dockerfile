FROM gradle:7.6.1-jdk17-alpine AS build

WORKDIR /app
COPY . .
RUN gradle build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Добавляем переменные окружения из MySQL
ENV DB_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}
ENV DB_USERNAME=${MYSQLUSER}
ENV DB_PASSWORD=${MYSQLPASSWORD}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
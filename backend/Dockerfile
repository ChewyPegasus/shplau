# Этап сборки
FROM gradle:7.6.1-jdk17-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# Этап выполнения
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Переменные окружения для подключения к базе данных
ENV DB_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}
ENV DB_USERNAME=${MYSQLUSER}
ENV DB_PASSWORD=${MYSQLPASSWORD}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

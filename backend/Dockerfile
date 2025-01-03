# Этап сборки
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app

# Устанавливаем дополнительные зависимости для совместимости
USER root
RUN apt-get update && apt-get install -y --no-install-recommends \
    libc6 \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# Этап выполнения
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Переменные окружения для подключения к базе данных
ENV DB_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}
ENV DB_USERNAME=${MYSQLUSER}
ENV DB_PASSWORD=${MYSQLPASSWORD}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

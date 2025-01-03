FROM gradle:8.3-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle wrapper
RUN ./gradlew build -x test --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENV DB_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}
ENV DB_USERNAME=${MYSQLUSER}
ENV DB_PASSWORD=${MYSQLPASSWORD}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM gradle:8-jdk24 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x spotlessCheck -x test

FROM amazoncorretto:24 AS run
COPY --from=build /app/build/libs/app-*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

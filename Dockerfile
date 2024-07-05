# 첫 번째 스테이지: 빌드 스테이지
FROM gradle:jdk21 as builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

# 두 번째 스테이지: 실행 스테이지
FROM openjdk:21-slim

WORKDIR /app


COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

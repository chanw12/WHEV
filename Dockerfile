FROM gradle:jdk21-graal-jammy as builder

# MySQL 클라이언트 설치
RUN apt-get update && apt-get install -y --no-install-recommends mysql-client && rm -rf /var/lib/apt/lists/*

COPY build/libs/whev.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.ejar"]

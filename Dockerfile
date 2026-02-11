# 빌드 스테이지
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

COPY gradle.properties .
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew .
COPY plat-boot/build.gradle plat-boot/
COPY plat-data/build.gradle plat-data/
COPY plat-ai/build.gradle plat-ai/

# 의존성만 먼저 다운로드 (캐시용)
RUN chmod +x ./gradlew && \
    ./gradlew dependencies --no-daemon > /dev/null || true

# 그 후 전체 소스 복사
COPY . .

RUN ./gradlew :plat-boot:bootJar --no-daemon -x test

# 런타임 스테이지
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY --from=build /app/plat-boot/build/libs/*.jar ./main.jar

EXPOSE 8080
CMD ["java", "-jar", "main.jar"]
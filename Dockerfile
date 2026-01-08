# 1단계: 빌드 단계 (Build stage)
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Gradle Wrapper 관련 파일 복사
COPY gradlew gradlew.bat ./
COPY gradle/ ./gradle/

# 소스 코드 및 Gradle 설정 파일 복사
COPY build.gradle settings.gradle ./
COPY src ./src

# Gradle 빌드 실행
RUN chmod +x /app/gradlew
RUN ./gradlew clean build --no-daemon

# 2단계: 실행 단계 (Runtime stage)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 빌드 스테이지에서 생성된 jar 파일 복사
COPY --from=build /app/build/libs/*SNAPSHOT.jar token.jar

# 포트 8090 열기
EXPOSE 8082

# 애플리케이션 실행
CMD ["java", "-jar", "token.jar"]
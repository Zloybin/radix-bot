# ==========================================
# Stage 1 — Build application
# ==========================================

FROM gradle:8.10.2-jdk23 AS builder

WORKDIR /app

# Сначала копируем файлы Gradle
# Это позволяет Docker кэшировать зависимости
COPY build.gradle settings.gradle ./

# Загружаем зависимости
RUN gradle dependencies --no-daemon

# Затем копируем исходный код
COPY src ./src

# Собираем Spring Boot application
RUN gradle bootJar --no-daemon


# ==========================================
# Stage 2 — Runtime
# ==========================================

FROM eclipse-temurin:23-jre

WORKDIR /app

# Копируем готовый JAR из builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Запускаем Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
# -------- Build stage (JDK 8) --------
FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /build

# 拷贝多模块工程
COPY pom.xml ./
COPY jt808-api ./jt808-api
COPY jt808-core ./jt808-core
COPY jt808-database ./jt808-database
COPY jt808-server ./jt808-server

# 仅构建 server 及其依赖模块，并拷贝运行时依赖到 target/dependency
RUN mvn -pl jt808-server -am clean package dependency:copy-dependencies -DincludeScope=runtime -DskipTests

# -------- Runtime stage (JRE 8) --------
FROM eclipse-temurin:8-jre
LABEL maintainer="lingxcom"

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone

WORKDIR /app

# server 模块编译产物 + 运行依赖
COPY --from=build /build/jt808-server/target/jt808-server-*.jar /app/app.jar
COPY --from=build /build/jt808-server/target/dependency /app/lib

EXPOSE 18800 8808

# 项目不是 Spring Boot fat-jar，使用 classpath 方式启动
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-cp", "/app/app.jar:/app/lib/*", "com.lingx.jt808.server.DevApp"]
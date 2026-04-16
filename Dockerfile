# 第一阶段：构建环境 (可选，如果你想在容器内编译)
# 如果你已经在本地 build 好了 jar 包，可以直接从第 7 行开始
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 第二阶段：运行环境
FROM openjdk:17-jdk-slim
LABEL maintainer="lingxcom"

# 设置容器内的时区（解决日志时间不对的问题）
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 设置工作目录
WORKDIR /app

# 将本地编译好的 jar 包复制到镜像中
# 注意：tracseek-server.jar 换成你实际生成的 jar 文件名
COPY target/tracseek-server.jar app.jar

# 暴露端口（根据你的项目实际端口修改）
# JT808 默认常使用 808 端口，Web 管理端可能用 8080
EXPOSE 18800
EXPOSE 8808

# 启动命令
ENTRYPOINT ["java", "-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
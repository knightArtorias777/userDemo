# 使用一个基于 Alpine Linux 的轻量级 Java 运行环境作为基础镜像
FROM azul/zulu-openjdk:8

# 将本地的可执行 JAR 文件复制到容器中
COPY target/user-center-backend-0.0.1-SNAPSHOT.jar /app/user-center-backend-0.0.1-SNAPSHOT.jar

# 设置容器的工作目录
WORKDIR /app

EXPOSE 8080

# 设置容器启动时执行的命令
CMD ["java", "-jar", "user-center-backend-0.0.1-SNAPSHOT.jar"]
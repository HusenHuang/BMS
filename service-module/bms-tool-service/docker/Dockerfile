#SpringBoot 2.3.x之前打包镜像方式

# 指定基础镜像，这是分阶段构建的前期阶段
FROM openjdk:8u212-jdk-stretch as builder
# 执行工作目录
WORKDIR application
# 配置参数
ARG JAR_FILE=target/*.jar
# 将编译构建得到的jar文件复制到镜像空间中
COPY ${JAR_FILE} application.jar

# 正式构建镜像
ENTRYPOINT java ${JAVA_OPTS} -jar  application.jar ${SPRING_BOOT_OPTS}


#SpringBoot 2.3.x打包镜像方式
## 指定基础镜像，这是分阶段构建的前期阶段
#FROM openjdk:8u212-jdk-stretch as builder
## 执行工作目录
#WORKDIR application
## 配置参数
#ARG JAR_FILE=target/*.jar
## 将编译构建得到的jar文件复制到镜像空间中
#COPY ${JAR_FILE} application.jar
## 通过工具spring-boot-jarmode-layertools从application.jar中提取拆分后的构建结果
#RUN java -Djarmode=layertools -jar application.jar extract
#
## 正式构建镜像
#FROM openjdk:8u212-jdk-stretch
#WORKDIR application
## 前一阶段从jar中提取除了多个文件，这里分别执行COPY命令复制到镜像空间中，每次COPY都是一个layer
#COPY --from=builder application/dependencies/ ./
#COPY --from=builder application/spring-boot-loader/ ./
#COPY --from=builder application/application/ ./
#COPY --from=builder application/snapshot-dependencies/ ./
#
#ENTRYPOINT java \
#           -Duser.timezone=GMT+8 \
#           ${JAVA_OPTS} \
#           org.springframework.boot.loader.JarLauncher \
#           ${SPRING_BOOT_OPTS}
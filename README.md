# Restore

> 有人问我为什么 Windows linux 原生无法顺利执行？也有人说 Restore.exe 可以改名 idea64.exe webstorm64.exe，可以是万事万物。

## 项目简介

本项目是一个基于 Java21 的项目，用于自动化加密打包。
1.0 发布

## 项目使用说明

1. 项目使用 Maven 3.9.5 版本 进行管理。
   ```text
    PS D:\Project\Restore> mvn --version
    Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
    Maven home: D:\ProgramFiles\Scoop\apps\maven\current
    Java version: 21.0.1, vendor: GraalVM Community, runtime: D:\ProgramFiles\Scoop\apps\graalvm21-jdk21\current
    Default locale: zh_CN, platform encoding: UTF-8
    OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
   ```
2. jdk 需要使用 GraavlVM 版本
   ```text
   PS D:\Project\Restore> java -version
   openjdk version "21.0.1" 2023-10-17
   OpenJDK Runtime Environment GraalVM CE 21.0.1+12.1 (build 21.0.1+12-jvmci-23.1-b19)
   OpenJDK 64-Bit Server VM GraalVM CE 21.0.1+12.1 (build 21.0.1+12-jvmci-23.1-b19, mixed mode, sharing)
   ```
3. 配置文件
   ```yaml
   # 自定义最终产物密码
   password: zion
   # 自定义 svn 源码路径
   svn:
      - https://svn.zion/zion
   # 自定义 git 源码路径
   git:
     - https://gitee.zion/zion
   # 自定义本地源码路径
   path:
     - D:\Project\Restore
   ```

## 打包流程

1. 构建全量包，包含依赖

   ```shell
   mvn -P all clean package
   ```
2. 使用 agent 生成 [reflect-_config_.json](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Freflect-config.json) 等文件

   ```shell
   cp restore.yml ./target
   cd target
   java -agentlib:native-image-agent=config-output-dir=..\src\main\resources\META-INF\native-image -jar .\Restore-all.jar
   ```

   - [META-INF](src%2Fmain%2Fresources%2FMETA-INF)
     - [native-image](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image)
       - [agent-extracted-predefined-classes](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Fagent-extracted-predefined-classes)
       - [jni-config.json](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Fjni-config.json)
       - [predefined-classes-config.json](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Fpredefined-classes-config.json)
       - [proxy-config.json](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Fproxy-config.json)
       - [reflect-config.json](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Freflect-config.json)
       - [resource-config.json](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Fresource-config.json)
       - [serialization-config.json](src%2Fmain%2Fresources%2FMETA-INF%2Fnative-image%2Fserialization-config.json)
3. 打包 native
   有了刚刚生成的配置文件，打包 NativeImage，主要解决反射类加载问题，在构建时初始化

   ```shell
   mvn -P native clean package
   ```

### 参考

[graalvm 编译原生java 解决反射的问题 maven配置](https://www.cnblogs.com/cfas/p/16339789.html)

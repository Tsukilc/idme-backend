已有项目中部署数据建模引擎运行SDK操作指导
Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 1
前言
l 本课程主要介绍了如何部署数据建模引擎运行SDK以及事务协调器SDK；课程中可以了解到部署中的常见问题以及解决方式。l 本课程可以参考官方部署文档在Java工程中部署数据建模引擎运行SDK共同使用。
Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 2
课程目标
l 学完本课程后，您将能够：
p 掌握数据建模引擎运行SDK的部署
p 掌握数据建模引擎事务协调器SDK的部署p 了解部署过程中的常见问题
Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 3
目录
1. 检查必备资源
2. 获取数据建模引擎运行SDK
3. 部署数据建模引擎运行SDK
4. 部署数据建模引擎事务协调器SDK
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 4
   检查必备资源 – JAVA运行环境JAVA环境要求
   l JDK版本：
   p JDK 17
   l JDK 环境变量：
   p 将 JDK 安装路径配置到系统环境变量
   p 通过命令“java –version”获取到 JDK 的版本信息
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 5
   数据库资源要求
   l 数据库版本：
   p MySQL：5.x 或 8.x
   p PostgreSQL：13.x 或 14.x
   l 数据库：
   p 数据建模引擎SDK数据库（如：pdm）：用于存储SDK部署、使用过程中的数据（如：模型、实例）。p 本地服务单点登录数据库（如：pdm_ssf）：存储本地单点登录过程中的数据。n 注意事项：建议本地单点登录数据库库名为“{数据建模引擎SDK数据库库名}_ssf”的格式检查必备资源 –MySQL/PostgreSQL数据库
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 6
   数据库资源要求
   l 数据库版本：
   p Redis：5.x 或 6.x
   l 支持类型：
   p 单节点Redis数据库
   p 分布式缓存服务（DCS）
   l 注意事项：
   p Redis数据库建议设置密码
   检查必备资源 – Redis数据库
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 7
   l License文件：
   p 来源：SDK申请邮件获取
   p 作用：用作控制SDK的有效使用时间
   l 公钥：
   p 来源：SDK申请邮件获取
   p 作用：解析License文件
   l 注意事项：
   p Licesne文件不允许进行手动修改，会造成文件失效
   p 公钥需要手动配置在数据建模引擎运行SDK的环境变量中
   检查必备资源 – License文件和公钥
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 8
   目录
1. 检查必备资源
2. 获取数据建模引擎运行SDK
3. 部署数据建模引擎运行SDK
4. 部署数据建模引擎事务协调器SDK
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 9
   应用发布
   l 发布选择的JDK版本：
   p JDK 17
   l 发布步骤：
   p 登录华为云iDME 数据建模引擎控制台
   p iDME设计服务下选择后续进行SDK部署的应用进入
   p 执行应用发布（选择 JDK 17 进行发布）
   获取数据建模引擎运行SDK– 应用发布
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 10
   生成SDK
   l 生成SDK：
   p 应用发布完成后，拥有SDK权限的用户可以根据发布成功的记录生成SDK
   p 根据应用发布使用的JDK版本，会生成适合在不同JDK版本下部署的SDK包l 生成SDK步骤：
   p 进入完成应用发布的应用
   p 选择发布成功的发布记录，点击“生成SDK” -> “生成数据建模引擎运行SDK”p 等待 30s ~ 1min，完成SDK生成
   获取数据建模引擎运行SDK– 生成SDK
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 11
   下载SDK
   l 下载SDK：
   p 完成SDK生成后，可以在对应生成SDK的发布记录中，下载生成的SDK包l SDK组成部分：
   p lib：包含数据建模引擎运行SDK进行部署时的所有依赖
   p xdm-tx-distributor：事务协调器SDK进行部署时的所有依赖
   p README：部署SDK的指引文档和SDK版本特性文档
   获取数据建模引擎运行SDK– 下载SDK
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 12
   目录
1. 检查必备资源
2. 获取数据建模引擎运行SDK
3. 部署数据建模引擎运行SDK
4. 部署数据建模引擎事务协调器SDK
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 13
   部署项目&资源
   l 准备部署项目：
   p 部署项目能够独立启动，后续数据建模引擎SDK将会部署在此项目中l 解压SDK包：
   p 解压SDK包
   p 在已解压的SDK包下lib目录中提取出 microserviceTemplate.app jar包下的olc目录l 上传资源至部署项目
   p SDK包解压后的lib文件夹以及olc文件夹
   p Licesnse文件和公钥文件
   部署数据建模引擎运行SDK– 部署项目&资源
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 14
   环境变量
   l 部署项目resources目录下创建application-sdk.properties文件：p application-sdk.properties文件用于存放数据建模引擎运行SDK启动所需的所有环境变量。p 环境变量清单可以参考数据建模引擎运行SDK通用配置和数据建模引擎运行SDK自定义配置。p application-sdk.properties配置示例可以参考环境变量配置示例部署数据建模引擎运行SDK– 环境变量
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 15
   部署数据建模引擎运行SDK– 调整依赖&虚拟机参数调整依赖&虚拟参数
   l 项目pom文件需要排除与建模引擎冲突的依赖配置，如：
   p springboot相关依赖
   p 日志组件相关依赖
   p 数据库驱动依赖
   l 将resources目录下的lib添加为项目依赖
   l 在启动配置中添加虚拟机参数：--add-opens java.base/java.lang=ALL-UNNAMED--add-opensjava.base/java.util=ALL-UNNAMED --add-opens java.base/javax.crypto=ALL-UNNAMED--add-opens java.base/sun.security.util=ALL-UNNAMED --add-opens
   java.base/sun.security.x509=ALL-UNNAMED --add-opens java.base/sun.security.pkcs=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 16
   启动类
   l 启动类需要调整或新增部分注解用作数据建模引擎SDK运行：
   p @SpringBootApplication(exclude = {MongoAutoConfiguration.class, ElasticsearchClientAutoConfiguration.class})
   p @EnableAsync(proxyTargetClass = true)
   p @EnableScheduling
   p @ComponentScan(basePackages = {"com.huawei.it", "com.huawei.innovation", "com.huawei.iit","com.huawei.opendme", "com.huawei.xdm"})
   p @PropertySource(value = {"classpath:application.properties"})
   p @EnableCaching
   l 启动方法中添加生效环境变量文件配置
   p System.setProperty("spring.profiles.active", "sdk");
   部署数据建模引擎运行SDK– 调整启动类
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 17
   目录
1. 检查必备资源
2. 获取数据建模引擎运行SDK
3. 部署数据建模引擎运行SDK
4. 部署数据建模引擎事务协调器SDK
   Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 18
   环境变量
   l 在“{SDK解压目录}/xdm-tx-distributor”
   创建application.properties文件，
   配置示例如右侧：
   部署数据建模引擎事务协调器SDK–环境变量# 服务端口
   server.port=6001
# 服务名称
SERVICE_NAME=General
# 需要事务协调器的部署应用id
APP_ID=d2c748678e9b4d15b49038f4104a03b0# Redis配置
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=123456
# jasypt秘钥，选填
jasypt.encryptor.password=
# 事务协调器的服务发现类型
xdm.tx-distributor.discovery-type=static
# 数据建模引擎运行SDK是否支持https
xdm.tx-distributor.static.instance-secure=false# 需要事务协调器的部署应用所在的实例地址xdm.tx-distributor.static.instances=127.0.0.1:8003
Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 19
启动脚本
l 在“{SDK解压目录}/xdm-tx-distributor”
创建starttx.bat文件，
启动脚本示例如右侧：
启动服务
l 双击“starttx.bat”文件，启动服务
l 可以通过查看“tx.log”文件，判断事务协调器SDK是否成功启动部署数据建模引擎事务协调器SDK–脚本&启动服务:: 执行启动java命令，并把日志记录到当前目录的tx.log文件java --add-opens java.base/sun.security.util=ALL- UNNAMED --add-opens
java.base/sun.security.x509=ALL-UNNAMED--add-opens java.base/sun.security.pkcs=ALL-UNNAMED-jar xdm-tx-distributor.jar > tx.log 2>&1 &
Copyright © Huawei Technologies Co., Ltd. All rights reserved. Page 20
学习推荐
l 华为云官方网站
p 华为云官网：https://www.huaweicloud.com/
p 华为云开发者学堂：https://edu.huaweicloud.com/
华为云开发者学堂
版权所有©2024，华为技术有限公司，保留所有权利。
本资料是华为的保密信息，所有内容仅供华为授权的培训客户内部使用，禁止用于任何
其他用途。未经许可，任何人不得对本资料进行复制、修改、改编、也不得将本资料或
其任何部分或基于本资料的衍生作品提供给他人。 感谢
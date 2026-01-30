algorithm：python模型训练的代码和python后端的代码

backend：Java后端代码 jdk17 不然的话会报错，有个低版本的依赖
后端配置：
Monitoring-system/backend/src/main/resources/application-dev.yml 这个里面有数据库，redis的配置 配置好自己的密码
在后端backend打开，mvn clean install -DskipTests配置后端
mvn spring-boot:run 启动

city_app：安卓端前端代码

web：网页端前端代码
在前端web下打开，npm i 安装依赖
npm run serve 启动
提交前务必把前端的node_modules删掉！！！！！！！

iot：嵌入式代码


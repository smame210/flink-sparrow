# flink-sparrow

## 介绍
这是一个旨为快速构建flink任务的项目。

## 项目结构
```
flink-sparrow
├── README.md              # 项目说明
├── sparrow-api            # api接口
├── sparrow-common         # 公共模块
├── sparrow-core           # 核心模块，包含flink的启动入口
├── sparrow-connector      # 非官方的flink连接器
├── sparrow-plugin         # 自定义插件
├── sparrow-support        # 一些工具，例如脚本执行器 
``` 

## 编译
```
cd sparrow-package
mvn clean package -Dmaven.test.skip=true
``` 

## 运行
```
cd sparrow-package/target
tar -zxvf sparrow-package-${project.version}.tar.gz
cd sparrow-package-${project.version}/bin

# 上传 sparrow-core.jar 到flink集群, 然后添加运行参数并运行jar包
# 配置文件示例在conf目录下
./bin/flink run -c com.sparrow.core.starter.FlinkStarter /path/to/sparrow-core.jar -j ${example.json}
``` 

### 启动参数
```
-n/--name  任务名称
-f/--file  json配置文件路径
-j/--json  配置json(和-f参数二选一)
``` 
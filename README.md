# flink-sparrow

## Introduction
This is a simple project aimed at providing quick builds for Flink projects


## Structure
```
flink-sparrow
├── README.md              
├── sparrow-api            
├── sparrow-common         
├── sparrow-core           # include flink start main 
├── sparrow-connector      # include flink connectors   
├── sparrow-plugin         # custom plugins
├── sparrow-support        # some utils , e.g. script execution etc. 
``` 

## Build
```
cd sparrow-package
mvn clean package -Dmaven.test.skip=true
``` 

## Run
```
cd sparrow-package/target
tar -zxvf sparrow-package-${project.version}.tar.gz
cd sparrow-package-${project.version}/bin

# upload the sparrow-core.jar to flink, then run the jar with config json
# config json example in the config folder
./bin/flink run -c com.sparrow.core.starter.FlinkStarter /path/to/sparrow-core.jar -j ${example.json}
``` 

### 启动参数
```
-n/--name  job name
-f/--file  json config file path
-j/--json  json config string
``` 



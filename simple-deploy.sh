# 1.拉取代码
git clone https://github.com/husenhuang/bms

# 2.编译打包
mvn clean install -Dmaven.test.skip=true

# 3.构建docker镜像
cd /worker/git-worker/BMS/service-module/bms-tool-service
docker build -f docker/Dockerfile -t bms-tool-service:1.0 .

cd /worker/git-worker/BMS/service-module/bms-authority-service
docker build -f docker/Dockerfile -t bms-authority-service:1.0 .

cd /worker/git-worker/BMS/gateway-module/bms-gateway
docker build -f docker/Dockerfile -t bms-gateway:1.0 .

# 4. kubernetes部署
cd /worker/git-worker/BMS
kubectl apply -f service-module/bms-tool-service/kubernetes/
kubectl apply -f service-module/bms-authority-service/kubernetes/
kubectl apply -f gateway-module/bms-gateway/kubernetes/
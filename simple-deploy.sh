# 1.停止当前实例
cd /data/config/bms/BMS
kubectl delete -f service-module/bms-tool-service/kubernetes/
kubectl delete -f service-module/bms-authority-service/kubernetes/
kubectl delete -f gateway-module/bms-gateway/kubernetes/

# 2.编译打包
mvn clean install -Dmaven.test.skip=true

# 3.构建docker镜像
cd /data/config/bms/BMS/service-module/bms-tool-service
docker build -f docker/Dockerfile -t bms-tool-service:1.0 .

cd /data/config/bms/BMS/service-module/bms-authority-service
docker build -f docker/Dockerfile -t bms-authority-service:1.0 .

cd /data/config/bms/BMS/gateway-module/bms-gateway
docker build -f docker/Dockerfile -t bms-gateway:1.0 .

# 4. kubernetes部署
cd /data/config/bms/BMS
kubectl apply -f service-module/bms-tool-service/kubernetes/
kubectl apply -f service-module/bms-authority-service/kubernetes/
kubectl apply -f gateway-module/bms-gateway/kubernetes/

# 5. over
node("master") {
}

pipeline {

     agent any


     stages {
//          stage('Git阶段') {
//             steps {
//                git 'https://github.com/HusenHuang/BMS.git'
//             }
//         }
//
//         stage('Maven阶段') {
//             steps {
//                sh "mvn -T4 clean package -Dmaven.test.skip=true"
//             }
//         }
//
//         stage('生成镜像') {
//             steps {
//                 sh "docker build -f docker/Dockerfile -t bms-tool-service:1.0 .";
//             }
//         }
        stage('完成阶段') {
            steps {
               sh "echo ${project} OK"
            }
        }
     }
}
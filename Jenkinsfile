node("master") {
//     checkout scm
//     加载 groovy 脚本
//     resolutionApplicationUtils = load 'groovy/ResolutionApplicationUtils.groovy'
//     helmUtils = load 'groovy/HelmUtils.groovy'
}


pipeline {
     stages {
         stage('Git阶段') {
            steps {
               git 'https://github.com/HusenHuang/BMS.git'
            }
        }

        stage('Maven阶段') {
            steps {
               sh "mvn -T4 –C clean package -Dmaven.test.skip=true"
            }
        }


        stage('完成阶段') {
            steps {
               sh "echo OK"
            }
        }
     }
}
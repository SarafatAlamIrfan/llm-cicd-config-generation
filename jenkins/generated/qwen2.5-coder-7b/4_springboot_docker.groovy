pipeline {
    agent any
    tools {
        maven 'Default'
    }
    environment {
        PATH = "/usr/local/bin:$PATH"
    }
    stages {
        stage('Build Maven') {
            steps {
                checkout(scmGit([branches: [[name: '*/main']], credentialsId: 'saravanan81java', extensions: [], url: 'https://github.com/saravanan81java/devops-springboot']))
                sh 'mvn clean install'
            }
        }
        stage('Build docker image') {
            steps {
                script {
                    sh 'docker build -t saran81/devops-springboot-integration .'
                }
            }
        }
        stage('Push image to Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhubpwd', variable: 'dockerhubpwd')]) {
                        sh 'docker login -u saran81 -p $dockerhubpwd'
                        sh 'docker push saran81/devops-springboot-integration'
                    }
                }
            }
        }
    }
}

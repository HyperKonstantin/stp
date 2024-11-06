
properties([disableConcurrentBuilds()])

pipeline {
    agent any

    environment {
        SPRING_PROFILES_ACTIVE = 'deploy'
    }
    options {
        timestamps()
    }
    tools {
        maven 'Maven 3.8.6'
        jdk 'java 21'
    }

    stages {
        stage("test") {
            steps {
                sh 'mvn clean test'
            }
        }
        stage("kill old container") {
            steps {
                sh 'docker stop backend-service'
                sh 'docker rm backend-service'
            }
        }
        stage("build image") {
            steps {
                sh 'docker image rm backend'
                sh 'mvn clean package -DskipTests'
                sh 'docker build -t backend .'
            }
        }
        stage("deploy") {
            steps {
                sh 'docker run -p 8081:8080 --name backend-service -d backend'
            }
        }
        stage("health check") {
            steps {
                sh 'sleep 30'
                sh 'curl http://85.198.109.181:8081/about || exit 1'
            }
        }
    }
}

properties([disableConcurrentBuilds()])

pipeline {
    agent any

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
        stage("build image") {
            steps {
                sh 'docker rm -f backend'
                sh 'mvn clean package -DskipTests'
                sh 'docker build -t backend .'
            }
        }
        stage("kill old container") {
            steps {
                sh 'docker stop backend-service'
                sh 'docker rm backend-service'
            }
        }
        stage("deploy") {
            steps {
                sh 'docker run -p 8081:8080 --name backend-service -d backend'
            }
        }
    }
}
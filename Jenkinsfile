
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
        stage("build image") {
            steps {
                sh 'docker rm -f backend'
                sh 'mvn clean install'
                sh 'docker build -t backend .'
            }
        }
        stage("deploy") {
            steps {
                sh 'docker run -p 8081:8080 --name backend-service -d backend'
            }
        }
    }
}
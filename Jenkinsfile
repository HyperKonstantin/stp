
properties([disableConcurrentBuilds()])

pipeline {
    agent any

    environment {
        SPRING_PROFILES_ACTIVE = 'deploy'
    }
    options {
        timestamps()
    }
    parameters {
        booleanParam(defaultValue: false, name: 'REVERT', description: 'revert to the previous image')
        booleanParam(defaultValue: false, name: 'SKIP_TESTS', description: 'skip tests stage')
        booleanParam(defaultValue: false, name: 'SKIP_HEALTHCHECK', description: 'skip health check stage')
    }
    tools {
        maven 'Maven 3.8.6'
        jdk 'java 21'
    }

    stages {
        stage("test") {
            when {
                expression { !params.SKIP_TESTS }
            }
            steps {
//                 sh 'mvn clean test'
                sh 'sleep 1'
            }
        }

        stage("build image") {
            when {
                expression { !params.REVERT }
            }
            steps {
                script {
                    sh '''docker image inspect backend:current && ( \
                          (docker image rm backend:previous || echo ">>> [previous image not found] <<<") ; \
                          docker tag backend:current backend:previous ; \
                          docker image rm backend:current ) || \
                            echo ">>> [current image not found, previous pipeline was with revert?] <<<" '''

                    sh 'mvn clean package -DskipTests'
                    sh 'docker build -t backend:current .'
                }
            }
        }

        stage ("Revert") {
            when {
                expression { params.REVERT }
            }

            steps {
                sh 'docker stop backend-service'
                sh 'docker rm backend-service'
                sh '''docker inspect backend:current && \
                      docker image rm backend:current || ( \
                      echo ">>> [current image not found, it's double revert? (run previous image)] <<<" ; \
                      docker run -p 127.0.0.1:8081:8080 --name backend-service -d backend:previous ; \
                      return 1 )'''
                sh 'docker run -p 127.0.0.1:8081:8080 --name backend-service -d backend:previous'
            }
        }

        stage("deploy") {
            when {
                expression { !params.REVERT }
            }
            steps {
                sh '''docker stop backend-service && docker rm backend-service || \
                       echo ">>>[running container not found]<<<"'''
                sh ''
                sh '''docker image inspect backend:current && \
                      docker run -p 127.0.0.1:8081:8080 --name backend-service -d backend:current || ( \
                      echo ">>> [current image not found, run previous version as current] <<<" ; \
                      docker tag backend:previous backend:current ; \
                      docker run -p 127.0.0.1:8081:8080 --name backend-service -d backend:current )'''
            }
        }

        stage("health check") {
            when {
                expression { !params.SKIP_HEALTHCHECK }
            }
            steps {
//                 sh 'sleep 60'
//                 sh 'curl http://85.198.109.181 || exit 1'
                sh 'sleep 1'
            }
        }
    }
}

properties([disableConcurrentBuilds()])

pipeline {
    agent any

    environment {
        SPRING_PROFILES_ACTIVE = 'deploy'
        RUN_DEPLOY = true
    }
    options {
        timestamps()
    }
    parameters {
        booleanParam(defaultValue: false, name: 'REVERT', description: 'revert to the previous image')
    }
    tools {
        maven 'Maven 3.8.6'
        jdk 'java 21'
    }

    stages {
        stage("test") {
            when {
                not {
                    expression { params.REVERT }
                }
            }
            steps {
                sh 'mvn clean test'
            }
        }
        stage("build image") {
            when {
                not {
                    expression { params.REVERT }
                }
            }
            steps {
                script {
                    sh '''docker inspect backend:current && ( \
                          docker image rm backend:previous ; \
                          docker tag backend:current backend:previous ; \
                          docker image rm backend:current ) || echo ">>> [Building image after revert] <<<" '''

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
                script {
                    env.RUN_DEPLOY = sh script:'docker image rm backend:current', returnStdout: true
                }
            }
        }

        stage("kill old container") {
            when {
                expression { env.RUN_DEPLOY }
            }
            steps {
                sh 'docker stop backend-service'
                sh 'docker rm backend-service'
            }
        }

        stage("deploy") {
            when {
                expression { env.RUN_DEPLOY }
            }
            steps {
                script {
                    if (params.REVERT) {
                        sh 'docker run -p 8081:8080 --name backend-service -d backend:previous'
                    }
                    else {
                        sh 'docker run -p 8081:8080 --name backend-service -d backend:current'
                    }
                }
            }
        }

        stage("health check") {
            when {
                expression { env.RUN_DEPLOY }
            }
            steps {
                sh 'sleep 60'
                sh 'curl http://85.198.109.181:8081/about || exit 1'
            }
        }
    }
}
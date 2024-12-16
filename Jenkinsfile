
properties([disableConcurrentBuilds()])

def getProfileAndRunPort(branch_name){
    switch(branch_name) {
      case "main":
        return [
            "contour": "prod",
            "port": "8081"
        ]
        break
      case "dev":
        return [
            "contour": "dev",
            "port": "18081"
        ]
        break
      default:
        sh 'echo >>>[Unknown branch name]<<<'
        sh 'exit 1'
        break
    }
}

pipeline {
    agent any

    environment {
        BRANCH_NAME = "${GIT_BRANCH.split("/")[1]}"

        CONTOUR = "${getProfileAndRunPort(env.BRANCH_NAME)["contour"]}"
        PORT = "${getProfileAndRunPort(env.BRANCH_NAME)["port"]}"
        SPRING_PROFILES_ACTIVE = "${CONTOUR}"

        PROJECT_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
        IMAGE_NAME = "${PROJECT_NAME}_${CONTOUR}"
        CONTAINER_NAME = "${PROJECT_NAME}-service_${CONTOUR}"
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
                sh 'mvn clean test'
            }
        }

        stage("build image") {
            when {
                expression { !params.REVERT }
            }
            steps {
                script {
                    sh '''docker image inspect ${IMAGE_NAME}:current && ( \
                          (docker image rm ${IMAGE_NAME}:previous || echo ">>> [previous image not found] <<<") ; \
                          docker tag ${IMAGE_NAME}:current ${IMAGE_NAME}:previous ; \
                          docker image rm ${IMAGE_NAME}:current ) || \
                            echo ">>> [current image not found, previous pipeline was with revert?] <<<" '''

                    sh 'mvn clean package -DskipTests'
                    sh 'docker build -t ${IMAGE_NAME}:current .'
                }
            }
        }

        stage ("Revert") {
            when {
                expression { params.REVERT }
            }

            steps {
                sh '''docker stop ${CONTAINER_NAME} && docker rm ${CONTAINER_NAME} || \
                       echo ">>>[running container not found]<<<"'''

                sh '''docker inspect ${IMAGE_NAME}:current && \
                      docker image rm ${IMAGE_NAME}:current || ( \
                      echo ">>> [current image not found, it's double revert? (run previous image)] <<<" ; \
                      docker run -p 127.0.0.1:${PORT}:8080 -e SPRING_PROFILES_ACTIVE=${CONTOUR} --name ${CONTAINER_NAME} -d ${IMAGE_NAME}:previous ; \
                      return 1 )'''
                sh 'docker run -p 127.0.0.1:${PORT}:8080 -e SPRING_PROFILES_ACTIVE=${CONTOUR} --name ${CONTAINER_NAME} -d ${IMAGE_NAME}:previous'
            }
        }

        stage("deploy") {
            when {
                expression { !params.REVERT }
            }
            steps {
                sh '''docker stop ${CONTAINER_NAME} && docker rm ${CONTAINER_NAME} || \
                       echo ">>>[running container not found]<<<"'''
                sh ''
                sh '''docker image inspect ${IMAGE_NAME}:current && \
                      docker run -p 127.0.0.1:${PORT}:8080 -e SPRING_PROFILES_ACTIVE=${CONTOUR} --name ${CONTAINER_NAME} -d ${IMAGE_NAME}:current || ( \
                      echo ">>> [current image not found, run previous version as current] <<<" ; \
                      docker tag ${IMAGE_NAME}:previous ${IMAGE_NAME}:current ; \
                      docker run -p 127.0.0.1:${PORT}:8080 -e SPRING_PROFILES_ACTIVE=${CONTOUR} --name ${CONTAINER_NAME} -d ${IMAGE_NAME}:current )'''
            }
        }

        stage("health check") {
            when {
                expression { !params.SKIP_HEALTHCHECK }
            }
            steps {
                sh 'sleep 60'
                sh 'curl http://127.0.0.1:${PORT} || exit 1'
            }
        }
    }
}
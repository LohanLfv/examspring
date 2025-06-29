pipeline {
    agent any

    tools {
        maven 'localMaven'
        jdk 'Java17'
    }

    environment {
        GITHUB_CREDENTIALS_ID = 'github-token'
        GIT_REPO_URL = 'https://github.com/LohanLfv/examspring.git'
        // Nom complet de votre image sur Docker Hub
        DOCKER_IMAGE_NAME = 'lohanlfv/examspringbook'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'
        SONAR_TOKEN_ID = 'sonarqube-token'
        SONAR_HOST_URL = 'http://192.168.244.147:9000'
    }

    stages {
        stage('1. Checkout') {
            steps {
                git branch: 'main', credentialsId: env.GITHUB_CREDENTIALS_ID, url: env.GIT_REPO_URL
            }
        }

        stage('2. Build & Test') {
            steps {
                sh "mvn clean package"
            }
        }

        stage('3. SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: env.SONAR_TOKEN_ID, variable: 'SONAR_TOKEN')]) {
                    sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=examspring-lohan \
                          -Dsonar.host.url=${env.SONAR_HOST_URL} \
                          -Dsonar.login=${SONAR_TOKEN}
                    """
                }
            }
        }

        stage('4. Build Docker Image') {
            steps {
                script {
                    def imageTag = "build-${BUILD_NUMBER}"
                    env.FULL_IMAGE_NAME = "${env.DOCKER_IMAGE_NAME}:${imageTag}"
                    sh "docker build -t ${env.FULL_IMAGE_NAME} ."
                }
            }
        }

        stage('5. Scan Docker Image (Trivy)') {
            steps {
                sh "trivy image --exit-code 1 --severity CRITICAL,HIGH ${env.FULL_IMAGE_NAME}"
            }
        }

        stage('6. Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${env.FULL_IMAGE_NAME}"
                }
            }
        }

        stage('7. Deploy') {
            steps {
                script {
                    sh "docker stop examspring-app || true"
                    sh "docker rm examspring-app || true"
                    sh "docker run -d --name examspring-app -p 8080:8080 ${env.FULL_IMAGE_NAME}"
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout'
        }
    }
}

pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:8.11.1-jdk21-jammy
    command:
    - cat
    tty: true
    resources:
      requests:
        memory: "2Gi"
        cpu: "1"
      limits:
        memory: "4Gi"
        cpu: "2"
"""
        }
    }

    environment {
        VERSION = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        DOCKER_HUB_IMAGE = System.getenv("DOCKER_HUB_IMAGE") ?: "webdev0594/moco-backend"
        APP_PROFILE = System.getenv("APP_PROFILE") ?: "prod"
        PORT = System.getenv("PORT") ?: "8080"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                container('gradle') {
                    sh "./gradlew :application:test --parallel"
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                container('gradle') {
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: 'dockerhub-credential',
                            usernameVariable: 'DOCKER_HUB_USERNAME',
                            passwordVariable: 'DOCKER_HUB_PASSWORD'
                        )]) {
                            sh """
                                ./gradlew clean :application:jib \
                                    -Dorg.gradle.jvmargs="-Xmx2048m -XX:MaxMetaspaceSize=512m" \
                                    -Djib.to.image=${DOCKER_HUB_IMAGE} \
                                    -Djib.to.tags=latest,${VERSION} \
                                    -Djib.to.auth.username=${DOCKER_HUB_USERNAME} \
                                    -Djib.to.auth.password=${DOCKER_HUB_PASSWORD}
                            """
                        }
                    }
                }
            }
        }

        stage('Update Helm Values') {
            steps {
                container('gradle') {
                    withCredentials([usernamePassword(credentialsId: 'github-credential', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh """
                            rm -rf deploy-repo
                            git clone https://\${GIT_USERNAME}:\${GIT_PASSWORD}@github.com/dygma0/moco-backend.git deploy-repo
                            cd deploy-repo
                            sed -i 's/tag: ".*"/tag: "${VERSION}"/g' deploy/values.yaml
                            git config --global user.email "webdev0594@gmail.com" 
                            git config --global user.name "dygma0"
                            git add deploy/values.yaml
                            git commit -m "🚀 BEHOLD! The grand deployment of version ${VERSION} has arrived! 🎉"
                            git push origin main
                        """
                    }
                }
            }
        }
    }
}

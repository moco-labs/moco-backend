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

    parameters {
        string(name: 'APP_PROFILE', defaultValue: 'prod', description: 'Application profile to use (e.g., dev, prod)')
        booleanParam(name: 'PERFORM_DEPLOYMENT', defaultValue: false, description: 'Check to perform deployment')
    }

    environment {
        VERSION = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        DOCKER_HUB_IMAGE = "webdev0594/moco-backend"
        PORT = "8080"
    }

    options {
        timeout(time: 1, unit: 'HOURS')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    echo "Running with profile: ${params.APP_PROFILE}"
                    echo "Deployment will be performed: ${params.PERFORM_DEPLOYMENT}"
                }
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            options {
                timeout(time: 15, unit: 'MINUTES')
            }
            steps {
                container('gradle') {
                    sh "./gradlew :application:test --parallel"
                }
            }
        }

        stage('Build & Push Docker Image') {
            options {
                timeout(time: 20, unit: 'MINUTES')
            }
            steps {
                container('gradle') {
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: 'registry-credential',
                            usernameVariable: 'DOCKER_HUB_USERNAME',
                            passwordVariable: 'DOCKER_HUB_PASSWORD'
                        )]) {
                            sh """
                                ./gradlew clean :application:jib \
                                    -Djib.to.image=${DOCKER_HUB_IMAGE} \
                                    -Djib.to.tags=latest,${VERSION} \
                                    -Djib.to.auth.username=${DOCKER_HUB_USERNAME} \
                                    -Djib.to.auth.password=${DOCKER_HUB_PASSWORD} \
                                    -Djib.container.environment=SPRING_PROFILES_ACTIVE=${params.APP_PROFILE}
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { params.PERFORM_DEPLOYMENT == true }
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            stages {
                stage('Approve Production Deploy') {
                    when {
                        expression { params.APP_PROFILE == 'prod' }
                    }
                    steps {
                        input message: "Deploy to Production environment?", ok: "Deploy"
                    }
                }
                stage('Update Helm Values') {
                    steps {
                        container('gradle') {
                            withCredentials([usernamePassword(credentialsId: 'git-credential', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                                sh """
                                    rm -rf deploy-repo
                                    git clone https://\${GIT_USERNAME}:\${GIT_PASSWORD}@github.com/moco-labs/moco-chart.git deploy-repo
                                    cd deploy-repo
                                    sed -i 's/tag: ".*"/tag: "${VERSION}"/g' infrastructure/helm-charts/applications/backend/values.yaml
                                    git config --global user.email "404err@naver.com" 
                                    git config --global user.name "moco-labs"
                                    git add infrastructure/helm-charts/applications/backend/values.yaml
                                    git commit -m "update moco-backend version to ${VERSION}"
                                    git push origin main
                                """
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished. Cleaning up workspace.'
            deleteDir()
        }
        success {
            echo 'Pipeline succeeded!'
            // Slack/Email notification can be added here
        }
        failure {
            echo 'Pipeline failed!'
            // Slack/Email notification for failure can be added here
        }
    }
}

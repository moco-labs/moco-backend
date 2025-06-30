pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:8.14.2-jdk21-corretto
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
                    echo "Current branch: ${env.BRANCH_NAME}"
                    echo "PERFORM_DEPLOYMENT type: ${params.PERFORM_DEPLOYMENT.getClass()}"
                    echo "Branch condition check: ${env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop'}"
                }
            }
        }

        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: scm.branches,
                    extensions: [
                        [$class: 'SubmoduleOption',
                         disableSubmodules: false,
                         parentCredentials: true,
                         recursiveSubmodules: true,
                         reference: '',
                         trackingSubmodules: false
                        ]
                    ],
                    submoduleCfg: [],
                    userRemoteConfigs: scm.userRemoteConfigs
                ])
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

        stage('Check Deploy Conditions') {
            steps {
                script {
                    echo "=== Deploy Condition Check ==="
                    echo "PERFORM_DEPLOYMENT: ${params.PERFORM_DEPLOYMENT}"
                    echo "Current Branch: ${env.BRANCH_NAME}"
                    echo "Branch matches main: ${env.BRANCH_NAME == 'main'}"
                    echo "Branch matches develop: ${env.BRANCH_NAME == 'develop'}"
                    
                    def shouldDeploy = params.PERFORM_DEPLOYMENT == true && 
                                     (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop')
                    echo "Should deploy: ${shouldDeploy}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { params.PERFORM_DEPLOYMENT == true }
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
                                    git clone https://\${GIT_USERNAME}:\${GIT_PASSWORD}@github.com/moco-labs/moco-chart.git deploy-repo
                                    cd deploy-repo
                                    sed -i 's/tag: ".*"/tag: "${VERSION}"/g' infrastructure/helm-charts/applications/backend/values-prod.yaml
                                    git config --global user.email "me@mooowu.xyz" 
                                    git config --global user.name "moco-ci"
                                    git add infrastructure/helm-charts/applications/backend/values-prod.yaml
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
            echo 'Pipeline finished.'
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

pipeline {
    agent any
    environment {
        REGISTRY = 'harbor.company.com/devops'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -T 2C -DskipTests clean package'
            }
        }
        stage('Docker Build & Push') {
            steps {
                dir('service-user') {
                    sh "docker build -t ${REGISTRY}/service-user:latest ."
                    sh "docker push ${REGISTRY}/service-user:latest"
                }
                dir('service-order') {
                    sh "docker build -t ${REGISTRY}/service-order:latest ."
                    sh "docker push ${REGISTRY}/service-order:latest"
                }
            }
        }
        stage('K8s Deploy') {
            steps {
                sh 'kubectl apply -f k8s/'
            }
        }
    }
}
pipeline {
    agent any

    environment {
            DOCKER_IMAGE = 'codethestudent/jle-token-service:latest'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/shoppingmall-platform/token-service.git'
            }
        }

        stage('Update application') {
            steps {
                script {
                    sh """
                    echo "> application 파일 위치로 이동"
                    cd src/main/resources

                    echo "> application 서버전용 yml 변경"
                    sed -i "s#\\\${DB_HOST}#${DB_HOST}#" application-dev.yml
                    sed -i "s#\\\${JWT_PRIVATE_KEY}#${JWT_PRIVATE_KEY}#" application-dev.yml
                    sed -i "s#\\\${JWT_PUBLIC_KEY}#${JWT_PUBLIC_KEY}#" application-dev.yml
                    sed -i "s#\\\${SERVER_HOST}#${SERVER_HOST}#" application-dev.yml

                    sed -i "s#\\\${DB_HOST}#${DB_HOST}#" application-prod.yml
                    sed -i "s#\\\${JWT_PRIVATE_KEY}#${JWT_PRIVATE_KEY}#" application-prod.yml
                    sed -i "s#\\\${JWT_PUBLIC_KEY}#${JWT_PUBLIC_KEY}#" application-prod.yml
                    sed -i "s#\\\${SERVER_HOST}#${SERVER_HOST}#" application-prod.yml
                    """
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    sh """
                    echo "> 권한 추가 및 클린 빌드"
                    chmod 777 ./gradlew
                    ./gradlew clean
                    ./gradlew build
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com/', 'DOCKER_HUB_CREDENTIALS') {
                        def image = docker.image("${DOCKER_IMAGE}")
                        sh "docker buildx create --use --name multiarch"
                        sh """
                        docker buildx build \
                        --platform linux/amd64,linux/arm64 \
                        -t ${image.imageName()} \
                        --push .
                        """
                    }
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                sshagent(['SSH_CREDENTIALS']) {
                    withCredentials([usernamePassword(credentialsId: 'GITHUB_CREDENTIALS', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                        sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@${DEPLOY_SERVER} '
                            cd docker-compose &&
                            git config credential.helper store &&
                            git pull https://${GIT_USER}:${GIT_PASS}@github.com/shoppingmall-platform/docker-compose.git main &&
                            docker pull ${DOCKER_IMAGE} &&
                            docker compose down &&
                            docker compose up -d
                        '
                        """
                    }
                }
            }
        }
    }
}
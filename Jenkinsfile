pipeline {
    agent any

    tools {
        maven 'Maven_3.9'
        jdk 'JDK_17'
    }

    triggers {
        githubPush()
        cron('0 8 * * 1-5')
    }

    environment {
        ALLURE_RESULTS_DIR = 'target/allure-results'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                bat 'mvn clean compile -U'
            }
        }

        stage('Test') {
            steps {
                echo 'Injecting config and running API tests...'
                withCredentials([file(credentialsId: 'restassured-config', variable: 'CONFIG_FILE')]) {
                    bat 'copy "%CONFIG_FILE%" src\\test\\resources\\config.properties'
                    bat 'mvn test'
                }
            }
        }

        stage('Allure Report') {
            steps {
                echo 'Generating Allure report...'
            }
            post {
                always {
                    allure([
                        commandline: 'Allure',
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: "${ALLURE_RESULTS_DIR}"]]
                    ])
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully. All tests passed.'
        }
        failure {
            echo 'Pipeline failed. Check test results and Allure report.'
        }
        always {
            echo 'Cleaning workspace...'
            cleanWs()
        }
    }
}
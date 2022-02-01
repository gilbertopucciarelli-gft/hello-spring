pipeline {
    agent any

    stages {
        stage('Test') {
            sh './gradlew clean test'
        }

        post {
            always {
                junit 'build/test-results/test/*.xml'
                jacoco execPattern: 'build/jacoco/*.exec'
            }
        }

        stage('Build') {
            steps {
                // Run Gradle Wrapper
                sh './gradlew assemble'
            }

            post {
                success {
                    archiveArtifacts 'build/libs/*.jar'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...'
            }
        }
    }
}

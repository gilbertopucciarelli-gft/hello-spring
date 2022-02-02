pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh './gradlew clean test check'
            }

            post {
                always {
                    junit 'build/test-results/test/*.xml'
                    jacoco execPattern: 'build/jacoco/*.exec'
                    recordIssues(
                        tools: [
                            pmdParser(pattern: 'build/reports/pmd/*.xml')
                        ]
                    )
                }
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

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/gilbertopucciarelli-gft/hello_spring.git'

                // Run Gradle Wrapper
                sh './gradlew clean test assemble'

            }

            post {
                success {
                    junit 'build/test-results/test/*.xml'
                    archiveArtifacts 'build/libs/*.jar'
                    jacoco execPattern: 'build/jacoco/*.exec'
                }
            }
        }
    }
}

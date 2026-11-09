pipeline {
    agent any

    environment {
        // Lets Jenkins on macOS find mvn installed by Homebrew
        PATH = "/opt/homebrew/bin:/usr/local/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/kushalneupane-debug/computation_test.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Coverage Report') {
            steps {
                recordCoverage(
                    tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                    sourceCodeRetention: 'EVERY_BUILD',
                    qualityGates: [[
                        threshold: 95.0,
                        metric: 'LINE',
                        baseline: 'PROJECT',
                        criticality: 'FAILURE'
                    ]]
                )
            }
        }
    }
}

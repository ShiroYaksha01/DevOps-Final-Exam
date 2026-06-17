pipeline {
    agent any

    triggers {
        // Poll SCM every 5 minutes
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                // Auto build and test the application
                sh 'mvn clean test'
            }
        }

        stage('Deploy to Web Server') {
            steps {
                // Run Ansible Playbook to deploy after successful build and test
                sh 'ansible-playbook -i inventory.ini playbook.yml'
            }
        }
    }

    post {
        failure {
            // Send cc email to srengty@gmail.com and developer who committed
            emailext(
                subject: "Build Failed: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                body: """<p>The build has failed.</p>
                         <p>Check console output at <a href="${env.BUILD_URL}">${env.BUILD_URL}</a> to view the results.</p>""",
                to: "srengty@gmail.com",
                recipientProviders: [culprits()]
            )
        }
        success {
            echo 'Build, Test, and Deployment completed successfully.'
        }
    }
}
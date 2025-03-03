pipeline {
    agent any
    
    stages {
       
        stage('Compile') {
            steps {
                bat 'javac Main.java'
            }
        }
        
        stage('Test') {
            steps {
                bat 'Java Main'
            }
        }
    }
    
    post {
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build failed! Check the logs.'
        }
    }
}

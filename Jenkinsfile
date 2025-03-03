pipeline {
    agent any
    
    stages {
       
        stage('Compile') {
            steps {
                bat 'javac Add.java'
            }
        }
        
        stage('Test') {
            steps {
                bat 'Java Add'
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

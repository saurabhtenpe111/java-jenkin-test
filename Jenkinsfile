pipeline {
    agent any
    
    stages {
       
        stage('Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        
        stage('Package') {
            steps {
                bat 'mvn package'
            }
        }
        
        stage('Deploy') {
            steps {
                bat 'mvn deploy'
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

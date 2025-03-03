pipeline {
    agent any
    stages {
        stage("Compile") {
            steps {
                script {
                    sh 'javac Add.java'
                }
            }
        }
        stage("Run") {
            steps {
                script {
                    sh 'java Add'
                }
            }
        }
    }
}

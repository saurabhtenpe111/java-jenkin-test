pipeline{
  agent any
  stages{
    stage("compile"){
      steps{
        sh 'javac Add.java'
      }
    }
    stage("run"){
      steps{
        sh 'java Add'
      }
    }
  }
}

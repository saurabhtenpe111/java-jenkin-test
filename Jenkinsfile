pipeline{
  agent any
  stages{
    stage("compile"){
      sh 'javac Add.java'
    }
    stage("run"){
      sh 'java Add.java'
    }
  }
}

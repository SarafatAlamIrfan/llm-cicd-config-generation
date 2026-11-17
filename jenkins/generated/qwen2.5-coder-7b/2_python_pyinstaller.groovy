pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'python3 -m py_compile sources/add2vals.py sources/calc.py'
            }
        }
        stage('Test') {
            steps {
                sh 'py.test --verbose --junit-xml test-reports/results.xml sources/test_calc.py'
            }
        }
        stage('Deliver') {
            steps {
                sh 'pyinstaller --onefile sources/add2vals.py'
            }
        }
    }

    post {
        always {
            junit 'test-reports/results.xml'
        }
        success {
            archiveArtifacts artifacts: 'dist/add2vals', fingerprint: true
        }
    }
}

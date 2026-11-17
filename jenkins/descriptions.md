# Jenkins: descriptions

Each description was written from the original Jenkinsfile and checked claim by claim, in order, against it (75 checks, all pass) before it was given to the models.

## 1. Simple Java Maven App

- **Type:** Declarative pipeline
- **Original file:** [jenkins-docs/simple-java-maven-app/jenkins/Jenkinsfile](https://github.com/jenkins-docs/simple-java-maven-app/blob/d8c45db560796d68a6f4a0906c0b0fcfd81ab75e/jenkins/Jenkinsfile) (commit `d8c45db`)

> Write a Jenkins declarative pipeline (Jenkinsfile) for a Java Maven project. It runs on any agent whose label is not 'windows' (label '!windows') and uses the option skipStagesAfterUnstable(). It has three stages: 'Build', which runs the shell command mvn -B -DskipTests clean package; 'Test', which runs mvn test and, in a post 'always' block, publishes the JUnit test reports from target/surefire-reports/*.xml; 'Deliver', which runs the shell script ./jenkins/scripts/deliver.sh.

## 2. Simple Python PyInstaller App

- **Type:** Declarative pipeline
- **Original file:** [jenkins-docs/simple-python-pyinstaller-app/jenkins/Jenkinsfile](https://github.com/jenkins-docs/simple-python-pyinstaller-app/blob/b0a287b5df235396f347f507ae1b022be6386a68/jenkins/Jenkinsfile) (commit `b0a287b`)

> Write a Jenkins declarative pipeline (Jenkinsfile) for a small Python command-line app. It runs on any agent and uses the option skipStagesAfterUnstable(). It has three stages: 'Build', which byte-compiles the sources with python3 -m py_compile sources/add2vals.py sources/calc.py; 'Test', which runs py.test --verbose --junit-xml test-reports/results.xml sources/test_calc.py and, in a post 'always' block, publishes the JUnit results from test-reports/results.xml; 'Deliver', which builds a single-file executable with pyinstaller --onefile sources/add2vals.py and, in a post 'success' block, archives the artifact dist/add2vals.

## 3. Multibranch Pipeline Project (Node.js / React)

- **Type:** Declarative pipeline
- **Original file:** [jenkins-docs/building-a-multibranch-pipeline-project/jenkins/Jenkinsfile](https://github.com/jenkins-docs/building-a-multibranch-pipeline-project/blob/336f163993b7fe336e4f63acc0c09d994a06de2b/jenkins/Jenkinsfile) (commit `336f163`)

> Write a Jenkins declarative pipeline (Jenkinsfile) for a Node.js/React app built as a multibranch pipeline. The whole pipeline runs inside a Docker agent using the image 'node:lts-alpine' with the args '-p 3000:3000 -p 5000:5000'. Set the environment variable CI = 'true'. It has four stages: 'Build', which runs npm install; 'Test', which runs ./jenkins/scripts/test.sh; 'Deliver for development', which runs only when the branch is 'development': it runs ./jenkins/scripts/deliver-for-development.sh, then pauses with an input step whose message is 'Finished using the web site? (Click "Proceed" to continue)', then runs ./jenkins/scripts/kill.sh; 'Deploy for production', which runs only when the branch is 'production': it runs ./jenkins/scripts/deploy-for-production.sh, then the same input message, then ./jenkins/scripts/kill.sh.

## 4. DevOps Spring Boot (Maven + Docker Hub)

- **Type:** Declarative pipeline
- **Original file:** [saravanan81java/devops-springboot/Jenkinsfile](https://github.com/saravanan81java/devops-springboot/blob/b895da1cd02c810a202f91042b1f3f0aaf1ab5e9/Jenkinsfile) (commit `b895da1`)

> Write a Jenkins declarative pipeline (Jenkinsfile) that builds a Spring Boot app with Maven and pushes a Docker image to Docker Hub. It runs on any agent and uses the Maven tool installation named 'Default'. Set the environment variable PATH to "/usr/local/bin:$PATH" so Docker can be found. It has three stages: 'Build Maven', which checks out the code with checkout(scmGit(...)) from the main branch (*/main) of https://github.com/saravanan81java/devops-springboot using the credentials ID 'saravanan81java' and no extensions, then runs mvn clean install; 'Build docker image', which inside a script block runs docker build -t saran81/devops-springboot-integration . ; 'Push image to Hub', which inside a script block uses withCredentials with a secret text credential 'dockerhubpwd' bound to the variable dockerhubpwd to run docker login -u saran81 -p ${dockerhubpwd}, and then runs docker push saran81/devops-springboot-integration.

## 5. Spring Boot Docker Deploy (Scripted Pipeline)

- **Type:** Scripted pipeline
- **Original file:** [vdharmaraj/PGDO_Proj3/Jenkinsfile](https://github.com/vdharmaraj/PGDO_Proj3/blob/de3d1f97fd2292839041ae290cd6210f0771cb2c/Jenkinsfile) (commit `de3d1f9`)

> Write a Jenkins scripted pipeline (Jenkinsfile using node { ... }, not the declarative pipeline block) that builds a Spring Boot app, builds and pushes a Docker image, and redeploys it as a container. Inside node, define the variables application = "devopsexample", dockerhubaccountid = "vikidvg", mvnHome = tool 'maven-3.5.2', an empty dockerImage, and dockerImageTag = "${dockerhubaccountid}/${application}:${env.BUILD_NUMBER}". The stages are: 'Clone Repo', which runs git with url 'https://github.com/vdharmaraj/PGDO_Proj3.git' and branch 'main', then sets mvnHome = tool 'maven-3.5.2' again; 'Build Project', which runs "'${mvnHome}/bin/mvn' clean install"; 'Build Docker Image with new code', which sets dockerImage = docker.build("${dockerhubaccountid}/${application}:${env.BUILD_NUMBER}"); 'Push Image to Remote Repo', which echoes "Docker Image Tag Name ---> ${dockerImageTag}" and then, inside docker.withRegistry('', 'dockerHub'), pushes the image with the tag "${env.BUILD_NUMBER}" and with the tag "latest"; 'Remove running container with old code', which runs docker rm -f \$(docker ps -a -f name=devopsexample -q) || true; 'Deploy Docker Image with new changes', which runs docker run --name devopsexample -d -p 2222:2222 ${dockerhubaccountid}/${application}:${env.BUILD_NUMBER}; 'Remove old images', which runs docker rmi ${dockerhubaccountid}/${application}:latest -f.

---
The original files belong to their authors and are not copied here: each link opens the exact version used, pinned to its commit.

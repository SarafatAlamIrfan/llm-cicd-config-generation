# Azure: descriptions

Each description was written from the original YAML and checked claim by claim against it (121 checks, all pass) before it was given to the models.

## 1. Azure Pipeline Basic (Python + pytest)

- **Type:** Azure Pipelines
- **Original file:** [atulkamble/azure-pipeline-basic/azure-pipelines.yml](https://github.com/atulkamble/azure-pipeline-basic/blob/d771d2eebc40e33c6dc737e59cbbc14625229bc7/azure-pipelines.yml) (commit `d771d2e`)

> Create an Azure Pipelines YAML file (azure-pipelines.yml) for a Python project. The pipeline is triggered by commits to the main branch and runs on a Microsoft-hosted ubuntu-latest agent. Define a pipeline variable named pythonVersion with the value '3.11'. The pipeline has three steps: (1) the UsePythonVersion@0 task, selecting the Python version from the pythonVersion variable; (2) a script step with display name 'Install dependencies' that upgrades pip and installs the packages listed in requirements.txt; (3) a script step with display name 'Run tests' that runs pytest in quiet mode (pytest -q).

## 2. JavaScript E2E Express Server (Node.js + JUnit)

- **Type:** Azure Pipelines
- **Original file:** [Azure-Samples/js-e2e-express-server/azure-pipelines/junit-test-result.yml](https://github.com/Azure-Samples/js-e2e-express-server/blob/0c0c98f03b2e074dde10c21168a4416c89bde5a7/azure-pipelines/junit-test-result.yml) (commit `0c0c98f`)

> Create an Azure Pipelines YAML file for a Node.js Express server project. The pipeline is triggered by commits to the main branch and runs on a Microsoft-hosted ubuntu-latest agent. It has three steps: (1) the NodeTool@0 task installing Node.js version '10.x', with display name 'Install Node.js'; (2) a script step (no display name) that runs npm install, then npm run build, then npm test; (3) the PublishTestResults@2 task that publishes test results in JUnit format from files matching '**/TEST-*.xml'. This publish step has the display name 'npm install and build'.

## 3. Azure Batch Samples (Python, Java, .NET)

- **Type:** Azure Pipelines
- **Original file:** [Azure-Samples/azure-batch-samples/azure-pipelines.yml](https://github.com/Azure-Samples/azure-batch-samples/blob/402981098368ece3c51d864feb67c1336fa3bfbc/azure-pipelines.yml) (commit `4029810`)

> Create an Azure Pipelines YAML file that tests the Azure Batch sample code in three languages. The pipeline is triggered by commits to the master branch. Define three independent jobs, each running on a Microsoft-hosted windows-2019 agent. Job 'PythonTest' (display name 'Run lint tests') uses a matrix strategy with two configurations: PythonMinVersion with python.version '3.7' and PythonMaxVersion with python.version '3.10'. Its steps are: the UsePythonVersion@0 task with versionSpec '$(python.version)', then one script that upgrades setuptools, installs requirements from $(System.DefaultWorkingDirectory)/Python/Batch/requirements.txt, installs coveralls, flake8, pytest, pytest-cov and requests_mock, and runs flake8 on $(System.DefaultWorkingDirectory)/Python/Batch. Job 'JavaTest' has one Maven@3 task using the POM file $(System.DefaultWorkingDirectory)/Java/pom.xml, mavenOptions '-Xmx3072m', javaHomeOption 'JDKVersion', jdkVersionOption '1.8', jdkArchitectureOption 'x64', publishJUnitResults false, testResultsFiles '**/TEST-*.xml' and goals 'package'. Job 'DotNetTest' has two DotNetCoreCLI@2 tasks: the first runs the 'restore' command on '**/*.csproj' with feedsToUse 'select', the second runs the 'build' command on '**/*.csproj'.

## 4. Todo App: Node.js + MongoDB on Azure (azd deploy)

- **Type:** GitHub Actions (deploys to Azure)
- **Original file:** [Azure-Samples/todo-nodejs-mongo/.github/workflows/azure-dev.yml](https://github.com/Azure-Samples/todo-nodejs-mongo/blob/16695b06631a166eb5dc90ea2f0d52b917578013/.github/workflows/azure-dev.yml) (commit `16695b0`)

> Create a GitHub Actions workflow that deploys a Node.js + MongoDB todo application to Azure using the Azure Developer CLI (azd). The workflow runs on manual dispatch (workflow_dispatch) and on pushes to the main or master branch. Grant the permissions id-token: write and contents: read so it can log in with OpenID Connect federated credentials. It has one job named 'build' on ubuntu-latest with job-level environment variables AZURE_CLIENT_ID, AZURE_TENANT_ID and AZURE_SUBSCRIPTION_ID taken from repository variables (vars) and AZURE_CREDENTIALS taken from secrets. The steps are: 'Checkout' using actions/checkout@v4; 'Install azd' using Azure/setup-azd@v2; 'Install Nodejs' using actions/setup-node@v4 with node-version 18; 'Log in with Azure (Federated Credentials)', which runs only if AZURE_CLIENT_ID is not empty and runs azd auth login with the client ID, --federated-credential-provider "github" and the tenant ID in a pwsh shell; 'Log in with Azure (Client Credentials)', which runs only if AZURE_CREDENTIALS is not empty, parses the AZURE_CREDENTIALS JSON in pwsh, masks the client secret, and runs azd auth login with the client ID, client secret and tenant ID; 'Provision Infrastructure', which runs azd provision --no-prompt with environment variables AZURE_ENV_NAME, AZURE_LOCATION and AZURE_SUBSCRIPTION_ID from vars and AZD_INITIAL_ENVIRONMENT_CONFIG from secrets; 'Deploy Application', which runs azd deploy --no-prompt with AZURE_ENV_NAME, AZURE_LOCATION and AZURE_SUBSCRIPTION_ID from vars.

## 5. Java EE 7 Petstore: Deploy to Azure Web App

- **Type:** GitHub Actions (deploys to Azure)
- **Original file:** [Azure-Samples/Java-application-petstore-ee7/.github/workflows/workflow.yml](https://github.com/Azure-Samples/Java-application-petstore-ee7/blob/b60f9f236da4f609d9752caed1cc82dab33aeecf/.github/workflows/workflow.yml) (commit `b60f9f2`)

> Create a GitHub Actions workflow named 'Deploy Java Web App to Azure' that builds a Java EE 7 petstore application with Maven and deploys the WAR file to Azure App Service. It runs on push and on pull_request. Define workflow-level environment variables: AZURE_WEBAPP_NAME = JavaPetstore; AZURE_WEBAPP_PACKAGE_PATH = ${{ github.workspace }}; JAVA_VERSION = '1.8'; AZURE_WEBAPP_PUBLISH_PROFILE from the secret AZURE_WEBAPP_PUBLISH_PROFILE; CONTAINER_REGISTRY = ${{ secrets.REGISTRY_USERNAME }}.azurecr.io; AZURE_RESOURCE_GROUP = ActionsDemo; AZURE_APP_PLAN = ActionPlan. It has one job, 'build-and-deploy', on ubuntu-latest with these steps: check out the code with actions/checkout@master; 'Set up JDK 1.8' using actions/setup-java@v1 with java-version from JAVA_VERSION; 'Build with Maven' running mvn clean install --file pom.xml -Dmaven.test.skip=true; 'Azure authentication' using azure/login@v1 with creds from the secret AZURE_CREDENTIALS; 'Deploy to Azure WebApp' using azure/webapps-deploy@v2 with app-name from AZURE_WEBAPP_NAME and package '${{ env.AZURE_WEBAPP_PACKAGE_PATH }}/target/*.war'.

---
The original files belong to their authors and are not copied here: each link opens the exact version used, pinned to its commit.

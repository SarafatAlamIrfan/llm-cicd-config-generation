# LLM CI/CD Config Generation

Can a small, free, locally run code model write a correct CI/CD pipeline from a plain-English description?

This repo compares three open models served with [Ollama](https://ollama.com):

| Model | Size |
|---|---|
| `qwen2.5-coder:7b` | 4.7 GB |
| `qwen2.5-coder:14b` | 9.0 GB |
| `deepseek-coder-v2:16b` | 8.9 GB |

They were tested on three kinds of pipeline config: **GitHub Actions**, **Azure** (Azure Pipelines and GitHub Actions that deploy to Azure) and **Jenkins** (Groovy Jenkinsfiles).

## How it works

```
real config file on GitHub  →  description written from it  →  description checked against the file
        ↓                                                                     ↓
  compared by hand  ←  generated config  ←  3 models (same prompt, temperature 0, fixed seed)
```

1. **Pick 5 real projects** per platform that keep their own pipeline file in the repo.
2. **Write a description** of each file: every trigger, agent, stage/step, command and value it contains.
3. **Check the description** claim by claim against the real file, so a model is never blamed for a vague or wrong description (Azure: 121 checks, Jenkins: 75 checks, all pass).
4. **Generate** a config from each description with all 3 models. Same instruction for all, `temperature = 0`, `seed = 42`, so results can be reproduced.
5. **Compare** each generated file with the original and give a remark: *Correct*, *Mostly correct*, *Partially correct* or *Not correct*, with a comment on what differs.

## Repository layout

```
models/                      first experiments: one Colab notebook per model, GitHub Actions descriptions
azure/
  notebook/                  Kaggle notebook that runs all 3 models on the Azure descriptions
  descriptions.md            the 5 descriptions, each with a link to its original file
  generated/<model>/         the 5 YAML files each model produced
jenkins/
  notebook/                  Kaggle notebook that runs all 3 models on the Jenkins descriptions
  descriptions.md
  generated/<model>/         the 5 Jenkinsfiles each model produced
results/
  evaluation.xlsx            Summary, Azure and Jenkins tabs: descriptions, generated code, remarks, comments
  azure_results.csv          remarks and comments only (readable on GitHub)
  jenkins_results.csv
```

## Results

### Azure (YAML)

| # | Project | qwen 7b | qwen 14b | deepseek 16b |
|---|---|---|---|---|
| 1 | Azure Pipeline Basic (Python + pytest) | Correct | Correct | Correct |
| 2 | JS E2E Express Server (Node.js + JUnit) | Partially correct | Mostly correct | Correct |
| 3 | Azure Batch Samples (Python, Java, .NET) | Not correct | Correct | Partially correct |
| 4 | Todo App on Azure (azd deploy) | Not correct | Mostly correct | Not correct |
| 5 | Java EE 7 Petstore to Azure Web App | Partially correct | Correct | Mostly correct |

### Jenkins (Groovy)

| # | Project | qwen 7b | qwen 14b | deepseek 16b |
|---|---|---|---|---|
| 1 | Simple Java Maven App | Correct | Correct | Partially correct |
| 2 | Simple Python PyInstaller App | Partially correct | Mostly correct | Partially correct |
| 3 | Multibranch Pipeline (Node.js / React) | Not correct | Correct | Correct |
| 4 | DevOps Spring Boot (Maven + Docker Hub) | Not correct | Correct | Partially correct |
| 5 | Spring Boot Docker Deploy (scripted pipeline) | Partially correct | Partially correct | Not correct |

### Totals (Azure + Jenkins, 10 projects)

| Verdict | qwen 7b | qwen 14b | deepseek 16b |
|---|---|---|---|
| Correct | 2 | 6 | 3 |
| Mostly correct | 0 | 3 | 1 |
| Partially correct | 4 | 1 | 4 |
| Not correct | 4 | 0 | 2 |

Per-file comments are in [`results/`](results).

### Findings

- **qwen2.5-coder:14b is the most reliable**: 9 of 10 correct or mostly correct, and no file that would fail outright.
- **Size helps, but not alone**: deepseek-coder-v2:16b is bigger than qwen 14b and the fastest, but makes more structural mistakes (wrong input names, options placed as steps, invalid expressions).
- **Complex auth and scripted pipelines are hardest**: the azd login workflow (Azure #4) and the scripted Jenkinsfile (Jenkins #5) broke the most outputs. In Jenkins #5 every model put Groovy variables inside single quotes, so they are never filled in.
- **"Valid" is not "correct"**: all 15 Azure outputs parse as YAML and all 15 Jenkinsfiles pass a bracket/structure check, yet several would fail when actually run.

## Running it yourself

1. Open a notebook from `azure/notebook/` or `jenkins/notebook/` on **Kaggle** (File → Import Notebook).
2. Settings: **Accelerator: GPU T4 x2**, **Internet: On**.
3. **Save Version → Save & Run All (Commit)**. It installs Ollama, pulls the 3 models and writes the outputs to `/kaggle/working` (about 15–25 minutes).

The notebooks in `models/` are the earlier single-model experiments and run in Google Colab with a GPU runtime.

## Credits

The original pipeline files belong to their authors and are **not copied** into this repo. `azure/descriptions.md` and `jenkins/descriptions.md` link to each one, pinned to the commit that was used:
[atulkamble/azure-pipeline-basic](https://github.com/atulkamble/azure-pipeline-basic),
[Azure-Samples/js-e2e-express-server](https://github.com/Azure-Samples/js-e2e-express-server),
[Azure-Samples/azure-batch-samples](https://github.com/Azure-Samples/azure-batch-samples),
[Azure-Samples/todo-nodejs-mongo](https://github.com/Azure-Samples/todo-nodejs-mongo),
[Azure-Samples/Java-application-petstore-ee7](https://github.com/Azure-Samples/Java-application-petstore-ee7),
[jenkins-docs/simple-java-maven-app](https://github.com/jenkins-docs/simple-java-maven-app),
[jenkins-docs/simple-python-pyinstaller-app](https://github.com/jenkins-docs/simple-python-pyinstaller-app),
[jenkins-docs/building-a-multibranch-pipeline-project](https://github.com/jenkins-docs/building-a-multibranch-pipeline-project),
[saravanan81java/devops-springboot](https://github.com/saravanan81java/devops-springboot),
[vdharmaraj/PGDO_Proj3](https://github.com/vdharmaraj/PGDO_Proj3).

The GitHub Actions descriptions used in `models/` are taken from the [GitHub Actions documentation](https://docs.github.com/en/actions).

---
Md Sarafat Alam Mozumder · East West University

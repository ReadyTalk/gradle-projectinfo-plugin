# gradle-projectinfo-plugin

[![Build Status](https://drone.io/github.com/ReadyTalk/gradle-plugins/status.png)](https://drone.io/github.com/ReadyTalk/gradle-plugins/latest)

This project provides a file called `project.properties` on your classpath (`sourceSets.main.output.resourcesDir`) that contains git and project information.

## Usage

    apply plugin: 'projectInfo'

The plugin will ensure that the `jar` or `war` plugin has already been applied.

Then inject the `generateProjectPropertiesFile` task into your build

    war.dependsOn generateProjectPropertiesFile

Optionally, you may configure the name of the project injected into the
`project.properties` file, or set the git repository URL using the `projectInfo`
configuration Closure.

    projectInfo {
      projectName = 'my-custom-project-name'
      repoUrl = 'custom-project.git'
    }

## Details

The following properties are placed in the `project.properties` file:

- `PROJECT_NAME`: Defaults to the name of your gradle project, but can be overridden
- `PROJECT_VERSION`: Version of project as defined by gradle
- `REVISION`: The SHA-1 git hash of the latest commit on your git repo
- `AUTHOR`: The name of the last committer
- `EMAIL`: Email of the last committer
- `DATE`: Date of the latest commit
- `MESSAGE`: The 'short' version of the git commit message (up to the first LF)
- `BRANCH`: The current branch of the repo
- `URL`: The repository URL (defaults to null)

This project will only re-generate the properties file if the latest commit hash to the repo differs from the one that already exists in the build directory.  This allows UP-TO-DATE checking on your builds.

## Contributing

If you find this useful and would like to make it more generic or add additional functionality, please submit a pull request, or open an issue!
>>>>>>> d83bf5bbe1d3c82bea99b58be8fbe02f7730128e

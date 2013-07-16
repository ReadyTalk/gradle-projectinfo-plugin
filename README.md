# Project Info Gradle Plugin

This project provides a file called `project.properties` on your classpath
that contains git scm information.

## Usage

    apply plugin: 'projectinfo'

Optionally, you may configure the name of the project injected into the
`project.properties` file, or set the git repository URL using the `projectInfo`
configuration Closure.

    projectInfo {
      projectName = 'my-custom-project-name'
      repoUrl = 'custom-project.git'
    }

## Roadmap

This is pretty custom tailored to CC projects, but is very open to be
customizeable in the future.  If you would like to use it or customize further,
please send a pull request.
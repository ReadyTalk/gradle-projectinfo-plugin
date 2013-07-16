package com.ecovate

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.ajoberstar.gradle.git.tasks.*


class ProjectInfoPlugin implements Plugin<Project> {

  def latestCommitPath

  void apply(Project project) {
    project.extensions.create("projectInfo", ProjectInfoExtension)

    latestCommitPath = "${project.buildDir}/projectInfo/temp.properties"

    project.task('getLatestCommitHash', type: GitLog) {
      maxCommits = 1

      doLast {
        ant.touch(file: latestCommitPath, mkdirs: true)
        log.each { commit ->
          ant.propertyfile(file: latestCommitPath) {
            ant.entry(key: 'curRev', value: commit.id)
            ant.entry(key: 'curName', value: project.projectInfo.projectName ?: project.name)
            ant.entry(key: 'curRepoUrl', value: project.projectInfo.repoUrl)
          }
        }
      }
    }

    project.task('projectInfo', type: GitBranchList) {
      dependsOn 'getLatestCommitHash', project.processResources

      ext.destDir = project.sourceSets.main.output.resourcesDir
      ext.destFile = "${destDir}/project.properties"

      outputs.upToDateWhen {
        def curRevFile = project.file(latestCommitPath)
        def projectInfoFile = project.file(destFile)

        if (curRevFile.exists() && projectInfoFile.exists()) {
          def curProps = new Properties(), prevProps = new Properties()

          curRevFile.withReader { reader -> 
            curProps.load(reader)
          }

          projectInfoFile.withReader { reader -> 
            prevProps.load(reader)
          }

          def revCheck = curProps.getProperty('curRev') == prevProps.getProperty('REVISION')
          def nameCheck = curProps.getProperty('curName') == prevProps.getProperty('PROJECT_NAME')
          def repoCheck = curProps.getProperty('curRepoUrl') == prevProps.getProperty('URL')

          return revCheck && nameCheck && repoCheck
        } else {
          return false
        }
      }

      doLast {
        // Time is returned in seconds and as an integer
        def time = new Date(workingBranch.commit.time * 1000L)
        
        def properties = [
          'PROJECT_NAME': project.projectInfo.projectName ?: project.name,
          'PROJECT_VERSION': project.version,
          'REVISION': workingBranch.commit.id,
          'AUTHOR': workingBranch.commit.committer.name,
          'EMAIL': workingBranch.commit.committer.email,
          'DATE': time,
          'MESSAGE': workingBranch.commit.shortMessage,
          'BRANCH': workingBranch.refName,
          'URL': project.projectInfo.repoUrl
        ]

        ant.propertyfile(file: destFile) {
          properties.each { key, value -> 
            ant.entry(key: key, value: value)
          }
        }
      }
    }
  }
}

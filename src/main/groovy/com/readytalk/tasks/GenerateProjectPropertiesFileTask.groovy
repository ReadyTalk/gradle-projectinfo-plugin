package com.readytalk.tasks
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction

class GenerateProjectPropertiesFileTask extends DefaultTask {

  def destDir
  def destFile
  Repository repository
  Git git

  GenerateProjectPropertiesFileTask() {
    repository = getRepository()
    git = new Git(repository)
    destDir = project.sourceSets.main.output.resourcesDir
    destFile = "project.properties"

    outputs.upToDateWhen {
      File projectPropsFile = new File(destDir, destFile)
      if (!projectPropsFile.exists()) {
        return false
      }

      Properties properties = new Properties();
      properties.load(projectPropsFile.newReader());
      def log = git.log().setMaxCount(1).call()
      def commit = log.iterator().next()
      def revEqual = properties['REVISION'].equals(commit.id.name)
      def curProjectName = project.projectInfo.projectName ?: project.name
      def curRepoUrl = project.projectInfo.repoUrl ?: ""
      def projectNameEqual = properties['PROJECT_NAME'].equals(curProjectName)
      def urlEqual = properties['URL'].equals(curRepoUrl)
      return revEqual && projectNameEqual && urlEqual
    }
  }

  @TaskAction
  void action() {
    FileRepository repo = getRepository()
    Git git = new Git(repo)

    Iterable<RevCommit> call = git.log().setMaxCount(1).call()

    def properties = new Properties()
    call.each { commit ->
      properties['PROJECT_NAME'] = project.projectInfo.projectName ?: project.name
      properties['PROJECT_VERSION'] = project.version
      properties['REVISION'] = commit.getId().getName()
      properties['AUTHOR'] = commit.authorIdent.name
      properties['EMAIL'] = commit.authorIdent.emailAddress
      properties['DATE'] = new Date(commit.commitTime * 1000L).toString()
      properties['MESSAGE'] = commit.shortMessage
      properties['BRANCH'] = repo.getFullBranch()
      properties['URL'] = project.projectInfo.repoUrl ?: ""
    }

    if (!destDir.exists()) {
      destDir.mkdirs()
    }

    properties.store(new File(destDir, destFile).newWriter(), null)
  }

  private FileRepository getRepository() {
    FileRepositoryBuilder builder = new FileRepositoryBuilder()
        .setGitDir(new File(project.rootProject.projectDir, ".git"))
        .readEnvironment()
        .findGitDir()
    Repository repo = builder.build();
    repo
  }


}

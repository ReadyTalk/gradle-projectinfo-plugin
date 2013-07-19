package com.readytalk.tasks
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.InitCommand
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class GenerateProjectPropertiesFileTest {

  private Project project

  @Before
  public void setUp() throws Exception {
    project = ProjectBuilder.builder().build()
    project.apply plugin: 'java'
    project.apply plugin: 'projectInfo'

    InitCommand initCommand = new InitCommand();
    Git gitRepo = initCommand.setDirectory(project.getProjectDir()).setBare(false).call();

    def file = new File(project.getProjectDir(), "README")
    file.write("Readme")

    def add = gitRepo.add()
    add.addFilepattern("README").call()

    def commit = gitRepo.commit()
    commit.setMessage("initial commit").call()
  }

  @Test
  public void testGeneratesPropertiesFile() {
    project.generateProjectPropertiesFile.execute()

    File file = new File(project.sourceSets.main.output.resourcesDir, "project.properties")
    assertTrue(file.exists())

    Properties props = new Properties()
    file.withReader { reader ->
      props.load(reader)
    }

    assertEquals(props.size(), 9)
  }
}

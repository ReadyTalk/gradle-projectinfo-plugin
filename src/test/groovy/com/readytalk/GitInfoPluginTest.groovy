package com.readytalk

import com.readytalk.tasks.GenerateProjectPropertiesFileTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class GitInfoPluginTest {

  @Test(expected = GradleException)
  public void testApplyNoJavaPlugin() {
    Project project = ProjectBuilder.builder().build()
    project.apply plugin: 'gitInfo'
  }

  @Test
  public void testApplyWithJavaPlugin() throws Exception {
    Project project = ProjectBuilder.builder().build()
    project.apply plugin: 'java'
    project.apply plugin: 'gitInfo'

    assertTrue(project.tasks.generateGitPropertiesFile instanceof GenerateProjectPropertiesFileTask)
  }

  @Test
  public void testApplyWithWarPlugin() throws Exception {
    Project project = ProjectBuilder.builder().build()
    project.apply plugin: 'war'
    project.apply plugin: 'gitInfo'

    assertTrue(project.tasks.generateGitPropertiesFile instanceof GenerateProjectPropertiesFileTask)
  }
}

package com.readytalk

import com.readytalk.tasks.GenerateProjectPropertiesFileTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class ProjectInfoPlugin implements Plugin<Project> {

  void apply(Project project) {
    if (!project.getPlugins().hasPlugin(JavaPlugin.class)) {
      throw new GradleException("The 'projectInfo' plugin requires the java or war plugin to be applied.")
    }
    project.extensions.create("projectInfo", ProjectInfoExtension)
    project.tasks.create('generateProjectPropertiesFile', GenerateProjectPropertiesFileTask)
  }
}

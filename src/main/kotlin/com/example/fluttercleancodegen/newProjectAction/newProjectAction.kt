package com.example.fluttercleancodegen.newProjectAction

import com.intellij.ide.impl.OpenProjectTask
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.File
import java.nio.file.Path

class NewProjectAction : AnAction() {
    private var flutterSdkPath: String = ""

    override fun actionPerformed(eventAction: AnActionEvent) {
        // Show SDK Selection Dialog if needed
        if (!showSdkSelectionDialog()) return

        val dialog = NewProjectDialog()
        if (dialog.showAndGet()) {
            val projectName = dialog.projectNameField.text.trim()
            val projectLocation = dialog.projectLocationField.text.trim()

            if (projectName.isEmpty() || projectLocation.isEmpty()) {
                Messages.showWarningDialog("Both project name and location are required.", "Invalid Input")
                return
            }

            val projectDirectory = File(projectLocation, projectName)
            if (projectDirectory.exists()) {
                Messages.showWarningDialog("The project directory already exists. Please choose a different name or location.", "Directory Exists")
                return
            }

            // Create Flutter project with loading indicator
            createFlutterProjectAsync(projectName, projectLocation)
        }
    }

    private fun showSdkSelectionDialog(): Boolean {
        val dialog = FlutterSdkDialog(flutterSdkPath)
        return if (dialog.showAndGet()) {
            flutterSdkPath = dialog.getSelectedSdkPath()
            true
        } else {
            false
        }
    }

    private fun createFlutterProjectAsync(projectName: String, projectLocation: String) {
        val projectDirectory = File(projectLocation, projectName)

        ProgressManager.getInstance().run(object : com.intellij.openapi.progress.Task.Modal(null, "Creating Flutter Project", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = "Creating Flutter project '$projectName'..."

                try {
                    val flutterCommand = "$flutterSdkPath${File.separator}bin${File.separator}flutter${getExecutableExtension()}"
                    val processBuilder = ProcessBuilder(flutterCommand, "create", projectName)
                    processBuilder.directory(File(projectLocation))
                    processBuilder.redirectErrorStream(true)

                    val process = processBuilder.start()
                    val output = process.inputStream.bufferedReader().use { it.readText() }
                    process.waitFor()

                    if (process.exitValue() == 0) {
                        ApplicationManager.getApplication().invokeLater {
                            Messages.showInfoMessage(
                                "Flutter project '$projectName' created successfully at ${projectDirectory.absolutePath}!",
                                "Success"
                            )
                            openProjectInIde(projectDirectory)
                        }
                    } else {
                        ApplicationManager.getApplication().invokeLater {
                            Messages.showErrorDialog("Failed to create Flutter project:\n$output", "Error")
                        }
                    }
                } catch (ex: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        Messages.showErrorDialog("An error occurred: ${ex.message}", "Error")
                    }
                }
            }
        })
    }

    private fun openProjectInIde(projectDirectory: File) {
        val virtualFile: VirtualFile? = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(projectDirectory)

        if (virtualFile != null) {
            ApplicationManager.getApplication().invokeLater {
                val openProjectTask = OpenProjectTask()
                ProjectManagerEx.getInstanceEx().openProject(projectDirectory.toPath(), openProjectTask)
            }
        } else {
            Messages.showErrorDialog("Failed to locate or refresh project directory.", "Error")
        }
    }

    private fun getExecutableExtension(): String {
        return if (System.getProperty("os.name").lowercase().contains("win")) ".bat" else ""
    }
}

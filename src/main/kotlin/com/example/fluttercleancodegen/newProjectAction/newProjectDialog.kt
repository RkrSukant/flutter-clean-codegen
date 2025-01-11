package com.example.fluttercleancodegen.newProjectAction

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import javax.swing.JTextField

class NewProjectDialog : DialogWrapper(true) {
    val projectNameField = JTextField()
    val projectLocationField = JTextField()

    init {
        title = "Create New Flutter Project"
        init()
        projectLocationField.isEditable = false
        isResizable = false

    }

    override fun createCenterPanel() = panel {
        row("Project Name:") {
            cell(projectNameField).horizontalAlign(HorizontalAlign.FILL)
                .validationOnApply { validateProjectName(projectNameField) }
        }
        row("Project Location:") {
            cell(projectLocationField)
                .resizableColumn().horizontalAlign(HorizontalAlign.FILL)
                .validationOnApply { validateProjectLocation(projectLocationField) }
            button("Browse...") {
                val descriptor = FileChooserDescriptor(
                    false, // Directories only
                    true,  // Existing files and directories
                    false, false, false, false
                ).withTitle("Select Project Location")
                    .withDescription("Choose a directory for the Flutter project.")
                val selectedFile: VirtualFile? = FileChooser.chooseFile(descriptor, null, null)

                if (selectedFile != null) {
                    projectLocationField.text = selectedFile.path
                }
            }
        }
    }.apply {
        preferredSize = java.awt.Dimension(600,100)
        preferredFocusedComponent = projectNameField
    }

    private fun validateProjectName(field: JTextField): ValidationInfo? {
        return if (field.text.isBlank()) {
            ValidationInfo("Project name cannot be empty", field)
        } else {
            null
        }
    }

    private fun validateProjectLocation(field: JTextField): ValidationInfo? {
        return if (field.text.isBlank()) {
            ValidationInfo("Project location must be selected", field)
        } else {
            null
        }
    }
}

package com.example.fluttercleancodegen.newProjectAction

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import java.io.File
import javax.swing.JTextField

class FlutterSdkDialog(private val initialSdkPath: String?) : DialogWrapper(true) {
    private val sdkField = JTextField(initialSdkPath.orEmpty())

    init {
        title = "Select Flutter SDK"
        setSize(600, 150)
        init()
    }

    override fun createCenterPanel() = panel {
        row("Flutter SDK Path:") {
            cell(sdkField)
                .horizontalAlign(HorizontalAlign.FILL)
                .validationOnInput { validateFlutterSdkPath(sdkField.text) }
            button("Browse...") {
                val descriptor = FileChooserDescriptor(
                    false, true, false, false, false, false
                ).withTitle("Select Flutter SDK Location")
                    .withDescription("Choose the directory containing the Flutter SDK.")
                val selectedFile: VirtualFile? = FileChooser.chooseFile(descriptor, null, null)
                if (selectedFile != null) {
                    sdkField.text = selectedFile.path.replace("/", File.separator)
                }
            }
        }
    }.apply {
        preferredSize = java.awt.Dimension(600, 150)
        preferredFocusedComponent = sdkField
    }

    override fun doValidate(): ValidationInfo? {
        return validateFlutterSdkPath(sdkField.text)
    }

    private fun validateFlutterSdkPath(path: String): ValidationInfo? {
        val flutterExecutable = File("$path${File.separator}bin${File.separator}flutter${getExecutableExtension()}")
        return if (flutterExecutable.exists() && flutterExecutable.canExecute()) {
            null
        } else {
            ValidationInfo("Invalid Flutter SDK path. Please select a valid directory.", sdkField)
        }
    }

    fun getSelectedSdkPath(): String = sdkField.text.trim()

    private fun getExecutableExtension(): String {
        return if (System.getProperty("os.name").lowercase().contains("win")) ".bat" else ""
    }
}

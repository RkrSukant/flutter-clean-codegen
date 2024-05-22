import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.util.ui.JBUI
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class NewProjectDialog : DialogWrapper(true) {

    private val projectNameField = JTextField(20)
    private val locationField = TextFieldWithBrowseButton()

    init {
        init()
        title = "New Project with Clean Architecture"
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(GridBagLayout()).apply {
            preferredSize = Dimension(500, 100)
            border = JBUI.Borders.empty(10)
        }

        val constraints = GridBagConstraints().apply {
            insets = JBUI.insets(5)
            anchor = GridBagConstraints.WEST
        }

        // Project Name Label
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.fill = GridBagConstraints.NONE
        constraints.weightx = 0.0
        panel.add(JLabel("Project Name:"), constraints)

        // Project Name Text Field
        constraints.gridx = 1
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.weightx = 1.0
        panel.add(projectNameField, constraints)

        // Project Location Label
        constraints.gridx = 0
        constraints.gridy = 1
        constraints.fill = GridBagConstraints.NONE
        constraints.weightx = 0.0
        panel.add(JLabel("Location:"), constraints)

        // Project Location Text Field with Browse Button
        constraints.gridx = 1
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.weightx = 1.0
        locationField.addBrowseFolderListener(
                "Choose Location",
                null,
                null,
                FileChooserDescriptorFactory.createSingleFolderDescriptor()
        )
        panel.add(locationField, constraints)

        return panel
    }

    override fun doOKAction() {
        val projectName = projectNameField.text
        val location = locationField.text
        Messages.showMessageDialog(
                "Project Name: $projectName\nLocation: $location",
                "Project Information",
                Messages.getInformationIcon()
        )
        super.doOKAction()
    }

    override fun isResizable(): Boolean {
        return false
    }
}

fun main() {
    SwingUtilities.invokeLater {
        NewProjectDialog().show()
    }
}

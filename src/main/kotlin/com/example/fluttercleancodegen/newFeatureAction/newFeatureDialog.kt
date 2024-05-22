import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.JBUI
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class NewFeatureDialog : DialogWrapper(true) {

    private val featureNameField = JTextField(20)

    init {
        init()
        title = "New Feature with Clean Architecture"
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(GridBagLayout()).apply {
            preferredSize = Dimension(500, 100)
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
        panel.add(JLabel("Feature Name:"), constraints)

        // Project Name Text Field
        constraints.gridx = 1
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.weightx = 1.0
        panel.add(featureNameField, constraints)

        return panel
    }

    override fun doOKAction() {
        val projectName = featureNameField.text
        Messages.showMessageDialog(
                "Feature Name: $projectName",
                "Feature Information",
                Messages.getInformationIcon()
        )
        super.doOKAction()
    }

    override fun isResizable(): Boolean {
        return false
    }
}
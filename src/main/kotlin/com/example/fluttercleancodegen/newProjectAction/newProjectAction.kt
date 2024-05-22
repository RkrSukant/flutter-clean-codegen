package com.example.fluttercleancodegen.newProjectAction

import NewProjectDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class NewProjectAction : AnAction() {
    override fun actionPerformed(eventAction: AnActionEvent) {
        val dialog = NewProjectDialog()
        dialog.isResizable = false
        dialog.show()
    }

}
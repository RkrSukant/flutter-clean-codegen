package com.example.fluttercleancodegen.newFeatureAction

import NewFeatureDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class NewFeatureAction : AnAction() {
    override fun actionPerformed(eventAction: AnActionEvent) {
        val dialog = NewFeatureDialog()
        dialog.show()
    }
}
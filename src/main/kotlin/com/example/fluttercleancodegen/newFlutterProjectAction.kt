package com.example.fluttercleancodegen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class NewFlutterProjectAction : AnAction(){
    override fun actionPerformed(e: AnActionEvent){
        val processBuilder = ProcessBuilder()
        processBuilder.command("flutter", "create", "project_name")
    }
}
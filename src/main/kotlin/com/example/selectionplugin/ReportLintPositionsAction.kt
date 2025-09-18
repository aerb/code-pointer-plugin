package com.example.selectionplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

class ReportLintPositionsAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        
        // Get PSI file
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return
        
        // Use the LintReporter to analyze and report issues
        LintReporter.reportLintIssues(project, editor, psiFile)
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        
        e.presentation.isEnabledAndVisible = project != null && 
                                           editor != null && 
                                           virtualFile != null &&
                                           editor.selectionModel.hasSelection()
    }
}

data class LintIssue(
    val type: String,
    val severity: String,
    val message: String,
    val startOffset: Int,
    val endOffset: Int,
    val lineNumber: Int
)
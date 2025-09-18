package com.example.selectionplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.util.text.StringUtil
import java.awt.datatransfer.StringSelection

class CopySelectionReferenceAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        
        // Get the selected text range
        val selectionModel = editor.selectionModel
        if (!selectionModel.hasSelection()) {
            return
        }
        
        val startOffset = selectionModel.selectionStart
        val endOffset = selectionModel.selectionEnd
        
        // Calculate line numbers from offsets
        val document = editor.document
        val startLine = document.getLineNumber(startOffset) + 1 // Convert to 1-based
        val endLine = document.getLineNumber(endOffset) + 1 // Convert to 1-based
        
        // Get relative file path from project root
        val projectBasePath = project.basePath ?: return
        val filePath = virtualFile.path
        val relativePath = if (filePath.startsWith(projectBasePath)) {
            filePath.substring(projectBasePath.length + 1) // +1 to remove leading slash
        } else {
            virtualFile.name
        }
        
        // Create the reference string
        val reference = if (startLine == endLine) {
            "$relativePath:$startLine"
        } else {
            "$relativePath:$startLine-$endLine"
        }
        
        // Copy to clipboard
        val clipboard = CopyPasteManager.getInstance()
        clipboard.setContents(StringSelection(reference))
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

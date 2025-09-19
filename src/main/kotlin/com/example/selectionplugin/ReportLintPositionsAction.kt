package com.example.selectionplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.StringSelection

class CopySelectionReferenceAction : AnAction() {

  override fun actionPerformed(e: AnActionEvent) {
    val project = e.project ?: return
    val editor = e.getData(CommonDataKeys.EDITOR) ?: return
    val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

    // Get the selected text range or cursor position
    val selectionModel = editor.selectionModel
    val startOffset: Int
    val endOffset: Int
    
    if (selectionModel.hasSelection()) {
      startOffset = selectionModel.selectionStart
      endOffset = selectionModel.selectionEnd
    } else {
      // No selection, use cursor position
      startOffset = editor.caretModel.offset
      endOffset = startOffset
    }

    // Calculate line numbers and column positions from offsets
    val document = editor.document
    val startLine = document.getLineNumber(startOffset) + 1 // Convert to 1-based
    val endLine = document.getLineNumber(endOffset) + 1 // Convert to 1-based
    
    // Calculate column positions
    val startLineStartOffset = document.getLineStartOffset(startLine - 1)
    val endLineStartOffset = document.getLineStartOffset(endLine - 1)
    val startColumn = startOffset - startLineStartOffset + 1 // Convert to 1-based
    val endColumn = endOffset - endLineStartOffset + 1 // Convert to 1-based

    // Get relative file path from project root
    val projectBasePath = project.basePath ?: return
    val filePath = virtualFile.path
    val relativePath = if (filePath.startsWith(projectBasePath)) {
      filePath.substring(projectBasePath.length + 1) // +1 to remove leading slash
    } else {
      virtualFile.name
    }

    // Create the reference string with columns
    val reference = if (startLine == endLine && startColumn == endColumn) {
      // Single position
      "$relativePath:$startLine:$startColumn"
    } else if (startLine == endLine) {
      // Same line, different columns
      "$relativePath:$startLine:$startColumn-$endColumn"
    } else {
      // Different lines
      "$relativePath:$startLine:$startColumn-$endLine:$endColumn"
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
      virtualFile != null
  }
}

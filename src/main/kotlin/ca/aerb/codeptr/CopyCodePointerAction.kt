package ca.aerb.codeptr

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.StringSelection

class CopyCodePointerAction : AnAction() {
  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

  override fun actionPerformed(e: AnActionEvent) {
    val project = e.project ?: return
    val editor = e.getData(CommonDataKeys.EDITOR) ?: return
    val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

    val selectionModel = editor.selectionModel
    val startOffset: Int
    val endOffset: Int
    if (selectionModel.hasSelection()) {
      startOffset = selectionModel.selectionStart
      endOffset = selectionModel.selectionEnd
    } else {
      startOffset = editor.caretModel.offset
      endOffset = startOffset
    }

    val document = editor.document
    val startLine = document.getLineNumber(startOffset) + 1
    val endLine = document.getLineNumber(endOffset) + 1

    val startLineStartOffset = document.getLineStartOffset(startLine - 1)
    val endLineStartOffset = document.getLineStartOffset(endLine - 1)
    val startColumn = startOffset - startLineStartOffset + 1 // Convert to 1-based
    val endColumn = endOffset - endLineStartOffset + 1 // Convert to 1-based

    val settings = CodePointerSettingsState.getInstance()
    val pathReferenceMode = settings.pathReferenceMode
    val includeColumnNumbers = settings.includeColumnNumbers

    val filePath = PathResolver.resolvePath(project, virtualFile, pathReferenceMode)

    val reference =
        generateReference(
            filePath, startLine, endLine, startColumn, endColumn, includeColumnNumbers)

    CopyPasteManager.getInstance().setContents(StringSelection(reference))
  }

  /** Generates the code pointer reference string based on the provided parameters. */
  private fun generateReference(
      filePath: String,
      startLine: Int,
      endLine: Int,
      startColumn: Int,
      endColumn: Int,
      includeColumnNumbers: Boolean,
  ): String {
    return when {
      startLine == endLine && (!includeColumnNumbers || startColumn == endColumn) -> {
        // Single position without columns or same column
        "$filePath:$startLine"
      }
      startLine == endLine -> {
        // Same line, different columns
        "$filePath:$startLine:$startColumn-$endColumn"
      }
      includeColumnNumbers -> {
        // Different lines with columns
        "$filePath:$startLine:$startColumn-$endLine:$endColumn"
      }
      else -> {
        // Different lines without columns
        "$filePath:$startLine-$endLine"
      }
    }
  }

  override fun update(e: AnActionEvent) {
    val project = e.project
    val editor = e.getData(CommonDataKeys.EDITOR)
    val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)

    e.presentation.isEnabledAndVisible = project != null && editor != null && virtualFile != null
  }
}

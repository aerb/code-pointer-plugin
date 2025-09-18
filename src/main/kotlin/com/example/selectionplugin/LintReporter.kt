package com.example.selectionplugin

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.openapi.util.TextRange

/**
 * Utility class for reporting lint issues in selected text
 */
class LintReporter {
    
    companion object {
        fun reportLintIssues(project: Project, editor: Editor, psiFile: PsiFile) {
            val selectionModel = editor.selectionModel
            if (!selectionModel.hasSelection()) {
                Messages.showInfoMessage(project, "Please select some text first", "No Selection")
                return
            }
            
            val startOffset = selectionModel.selectionStart
            val endOffset = selectionModel.selectionEnd
            val selectedText = selectionModel.selectedText ?: return
            
            // Run custom inspection on selected text
            val inspectionManager = InspectionManager.getInstance(project)
            val inspector = SelectionLintInspector()
            
            // Create a temporary inspection session for the selected range
            val problems = mutableListOf<ProblemDescriptor>()
            
            // Analyze the selected text
            val lintIssues = analyzeSelectedText(psiFile, startOffset, endOffset, selectedText)
            
            // Convert to ProblemDescriptors for display
            lintIssues.forEach { issue ->
                val problemDescriptor = inspectionManager.createProblemDescriptor(
                    psiFile,
                    TextRange(issue.startOffset, issue.endOffset),
                    issue.message,
                    when (issue.severity) {
                        "Error" -> com.intellij.codeInspection.ProblemHighlightType.ERROR
                        "Warning" -> com.intellij.codeInspection.ProblemHighlightType.WARNING
                        else -> com.intellij.codeInspection.ProblemHighlightType.WEAK_WARNING
                    },
                    false
                )
                problems.add(problemDescriptor)
            }
            
            // Display results
            displayLintResults(project, lintIssues, selectedText, problems)
        }
        
        private fun analyzeSelectedText(
            psiFile: PsiFile,
            startOffset: Int,
            endOffset: Int,
            selectedText: String
        ): List<LintIssue> {
            val issues = mutableListOf<LintIssue>()
            
            // Basic text analysis
            analyzeTextPatterns(selectedText, startOffset, issues)
            
            // PSI-based analysis
            analyzePsiElements(psiFile, startOffset, endOffset, issues)
            
            return issues
        }
        
        private fun analyzeTextPatterns(
            text: String,
            baseOffset: Int,
            issues: MutableList<LintIssue>
        ) {
            val lines = text.split("\n")
            
            lines.forEachIndexed { lineIndex, line ->
                val lineStartOffset = baseOffset + text.substring(0, lineIndex).length
                
                // Check line length
                if (line.length > 120) {
                    issues.add(
                        LintIssue(
                            type = "Code Style",
                            severity = "Warning",
                            message = "Line too long (${line.length} characters, max 120)",
                            startOffset = lineStartOffset,
                            endOffset = lineStartOffset + line.length,
                            lineNumber = lineIndex + 1
                        )
                    )
                }
                
                // Check for trailing whitespace
                if (line.endsWith(" ") || line.endsWith("\t")) {
                    val trailingStart = lineStartOffset + line.trimEnd().length
                    issues.add(
                        LintIssue(
                            type = "Code Style",
                            severity = "Warning",
                            message = "Trailing whitespace",
                            startOffset = trailingStart,
                            endOffset = lineStartOffset + line.length,
                            lineNumber = lineIndex + 1
                        )
                    )
                }
                
                // Check for hardcoded strings
                val stringPattern = "\"[^\"]*\"".toRegex()
                stringPattern.findAll(line).forEach { matchResult ->
                    val stringValue = matchResult.value
                    if (stringValue.length > 50 && !stringValue.contains("http")) {
                        issues.add(
                            LintIssue(
                                type = "Code Style",
                                severity = "Info",
                                message = "Consider externalizing long string literal",
                                startOffset = lineStartOffset + matchResult.range.first,
                                endOffset = lineStartOffset + matchResult.range.last + 1,
                                lineNumber = lineIndex + 1
                            )
                        )
                    }
                }
                
                // Check for TODO/FIXME comments
                if (line.contains("TODO") || line.contains("FIXME")) {
                    issues.add(
                        LintIssue(
                            type = "Code Quality",
                            severity = "Info",
                            message = "TODO/FIXME comment found",
                            startOffset = lineStartOffset,
                            endOffset = lineStartOffset + line.length,
                            lineNumber = lineIndex + 1
                        )
                    )
                }
            }
        }
        
        private fun analyzePsiElements(
            psiFile: PsiFile,
            startOffset: Int,
            endOffset: Int,
            issues: MutableList<LintIssue>
        ) {
            val startElement = psiFile.findElementAt(startOffset)
            val endElement = psiFile.findElementAt(endOffset - 1)
            
            if (startElement != null && endElement != null) {
                // Find the common parent element
                val commonParent = com.intellij.psi.util.PsiTreeUtil.findCommonParent(startElement, endElement)
                
                if (commonParent != null) {
                    // Analyze the common parent and its children
                    analyzeElementRecursively(commonParent, startOffset, endOffset, issues)
                }
            }
        }
        
        private fun analyzeElementRecursively(
            element: com.intellij.psi.PsiElement,
            startOffset: Int,
            endOffset: Int,
            issues: MutableList<LintIssue>
        ) {
            val elementStart = element.textRange.startOffset
            val elementEnd = element.textRange.endOffset
            
            // Only analyze elements that are within or overlap with the selection
            if (elementStart >= endOffset || elementEnd <= startOffset) {
                return
            }
            
            // Analyze current element
            analyzeElement(element, issues)
            
            // Recursively analyze children
            element.children.forEach { child ->
                analyzeElementRecursively(child, startOffset, endOffset, issues)
            }
        }
        
        private fun analyzeElement(element: com.intellij.psi.PsiElement, issues: MutableList<LintIssue>) {
            when (element) {
                is com.intellij.psi.PsiMethod -> analyzeMethod(element, issues)
                is com.intellij.psi.PsiVariable -> analyzeVariable(element, issues)
                is com.intellij.psi.PsiStatement -> analyzeStatement(element, issues)
            }
        }
        
        private fun analyzeMethod(method: com.intellij.psi.PsiMethod, issues: MutableList<LintIssue>) {
            val methodText = method.text
            val lineCount = methodText.split("\n").size
            
            if (lineCount > 50) {
                issues.add(
                    LintIssue(
                        type = "Code Quality",
                        severity = "Warning",
                        message = "Method is too long ($lineCount lines). Consider refactoring.",
                        startOffset = method.textRange.startOffset,
                        endOffset = method.textRange.endOffset,
                        lineNumber = 1
                    )
                )
            }
            
            val parameters = method.parameterList.parameters
            if (parameters.size > 5) {
                issues.add(
                    LintIssue(
                        type = "Code Quality",
                        severity = "Warning",
                        message = "Too many parameters (${parameters.size}). Consider using a parameter object.",
                        startOffset = method.parameterList.textRange.startOffset,
                        endOffset = method.parameterList.textRange.endOffset,
                        lineNumber = 1
                    )
                )
            }
        }
        
        private fun analyzeVariable(variable: com.intellij.psi.PsiVariable, issues: MutableList<LintIssue>) {
            val name = variable.name
            if (name != null && name.length > 30) {
                issues.add(
                    LintIssue(
                        type = "Code Style",
                        severity = "Info",
                        message = "Variable name '${name}' is quite long. Consider a shorter name.",
                        startOffset = variable.textRange.startOffset,
                        endOffset = variable.textRange.endOffset,
                        lineNumber = 1
                    )
                )
            }
        }
        
        private fun analyzeStatement(statement: com.intellij.psi.PsiStatement, issues: MutableList<LintIssue>) {
            when (statement) {
                is com.intellij.psi.PsiIfStatement -> {
                    val condition = statement.condition
                    if (condition != null && condition.text.length > 100) {
                        issues.add(
                            LintIssue(
                                type = "Code Quality",
                                severity = "Warning",
                                message = "Complex if condition. Consider extracting to a method.",
                                startOffset = condition.textRange.startOffset,
                                endOffset = condition.textRange.endOffset,
                                lineNumber = 1
                            )
                        )
                    }
                }
            }
        }
        
        private fun displayLintResults(
            project: Project,
            issues: List<LintIssue>,
            selectedText: String,
            problems: List<ProblemDescriptor>
        ) {
            if (issues.isEmpty()) {
                Messages.showInfoMessage(
                    project,
                    "No lint issues found in selected text.\n\nSelected text: \"${selectedText.take(100)}${if (selectedText.length > 100) "..." else ""}\"",
                    "Lint Analysis Complete"
                )
                return
            }
            
            val report = buildString {
                appendLine("🔍 Lint Analysis Report")
                appendLine("=" * 50)
                appendLine("Selected text: \"${selectedText.take(50)}${if (selectedText.length > 50) "..." else ""}\"")
                appendLine("Total issues found: ${issues.size}")
                appendLine()
                
                val groupedIssues = issues.groupBy { it.type }
                groupedIssues.forEach { (type, typeIssues) ->
                    appendLine("📋 $type (${typeIssues.size} issues)")
                    appendLine("-" * 30)
                    
                    typeIssues.forEach { issue ->
                        val severityIcon = when (issue.severity) {
                            "Error" -> "❌"
                            "Warning" -> "⚠️"
                            else -> "ℹ️"
                        }
                        appendLine("$severityIcon ${issue.message}")
                        appendLine("   Position: ${issue.startOffset}-${issue.endOffset}")
                        appendLine("   Line: ${issue.lineNumber}")
                        appendLine()
                    }
                }
                
                appendLine("💡 Tip: Use Ctrl+Alt+L to run this analysis on any selected text")
            }
            
            Messages.showInfoMessage(project, report, "Lint Analysis Results")
        }
    }
}
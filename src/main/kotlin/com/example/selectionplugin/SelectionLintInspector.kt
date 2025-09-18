package com.example.selectionplugin

import com.intellij.codeInspection.*
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange

/**
 * Custom inspection tool that analyzes selected text for lint issues
 */
class SelectionLintInspector : LocalInspectionTool() {
    
    override fun getDisplayName(): String = "Selection Lint Analysis"
    
    override fun getShortName(): String = "SelectionLint"
    
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                
                // Only analyze if this element is within the selected range
                if (isElementInSelection(element, holder.project)) {
                    analyzeElement(element, holder)
                }
            }
        }
    }
    
    private fun isElementInSelection(element: PsiElement, project: Project): Boolean {
        val editor = getCurrentEditor(project) ?: return false
        val selectionModel = editor.selectionModel
        
        if (!selectionModel.hasSelection()) return false
        
        val elementStart = element.textRange.startOffset
        val elementEnd = element.textRange.endOffset
        val selectionStart = selectionModel.selectionStart
        val selectionEnd = selectionModel.selectionEnd
        
        return elementStart >= selectionStart && elementEnd <= selectionEnd
    }
    
    private fun getCurrentEditor(project: Project): Editor? {
        val fileEditorManager = com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project)
        val selectedFiles = fileEditorManager.selectedFiles
        if (selectedFiles.isEmpty()) return null
        
        val fileEditor = fileEditorManager.getSelectedEditor(selectedFiles[0])
        return if (fileEditor is com.intellij.openapi.fileEditor.TextEditor) {
            fileEditor.editor
        } else null
    }
    
    private fun analyzeElement(element: PsiElement, holder: ProblemsHolder) {
        when (element) {
            is PsiMethod -> analyzeMethod(element, holder)
            is PsiVariable -> analyzeVariable(element, holder)
            is PsiStatement -> analyzeStatement(element, holder)
            is PsiExpression -> analyzeExpression(element, holder)
        }
    }
    
    private fun analyzeMethod(method: PsiMethod, holder: ProblemsHolder) {
        // Check method length
        val methodText = method.text
        val lineCount = methodText.split("\n").size
        if (lineCount > 50) {
            holder.registerProblem(
                method,
                "Method is too long ($lineCount lines). Consider breaking it into smaller methods.",
                ProblemHighlightType.WARNING
            )
        }
        
        // Check parameter count
        val parameters = method.parameterList.parameters
        if (parameters.size > 5) {
            holder.registerProblem(
                method.parameterList,
                "Too many parameters (${parameters.size}). Consider using a parameter object.",
                ProblemHighlightType.WARNING
            )
        }
        
        // Check for complex boolean expressions
        val returnStatements = PsiTreeUtil.findChildrenOfType(method, PsiReturnStatement::class.java)
        returnStatements.forEach { returnStmt ->
            val returnValue = returnStmt.returnValue
            if (returnValue is PsiBinaryExpression && isComplexBooleanExpression(returnValue)) {
                holder.registerProblem(
                    returnValue,
                    "Complex boolean expression. Consider extracting to a well-named method.",
                    ProblemHighlightType.WARNING
                )
            }
        }
    }
    
    private fun analyzeVariable(variable: PsiVariable, holder: ProblemsHolder) {
        // Check for unused variables
        if (variable.name != null && !isVariableUsed(variable)) {
            holder.registerProblem(
                variable.nameIdentifier ?: variable,
                "Variable '${variable.name}' is declared but never used.",
                ProblemHighlightType.WARNING
            )
        }
        
        // Check for overly long variable names
        if (variable.name != null && variable.name.length > 30) {
            holder.registerProblem(
                variable.nameIdentifier ?: variable,
                "Variable name '${variable.name}' is too long. Consider a shorter, clearer name.",
                ProblemHighlightType.WEAK_WARNING
            )
        }
    }
    
    private fun analyzeStatement(statement: PsiStatement, holder: ProblemsHolder) {
        when (statement) {
            is PsiIfStatement -> analyzeIfStatement(statement, holder)
            is PsiForStatement -> analyzeForStatement(statement, holder)
            is PsiWhileStatement -> analyzeWhileStatement(statement, holder)
        }
    }
    
    private fun analyzeIfStatement(ifStmt: PsiIfStatement, holder: ProblemsHolder) {
        val condition = ifStmt.condition
        if (condition is PsiBinaryExpression && isComplexBooleanExpression(condition)) {
            holder.registerProblem(
                condition,
                "Complex if condition. Consider extracting to a well-named method.",
                ProblemHighlightType.WARNING
            )
        }
        
        // Check for nested if statements
        val nestedIfs = PsiTreeUtil.findChildrenOfType(ifStmt.thenBranch, PsiIfStatement::class.java)
        if (nestedIfs.size > 2) {
            holder.registerProblem(
                ifStmt,
                "Deeply nested if statements. Consider using early returns or guard clauses.",
                ProblemHighlightType.WARNING
            )
        }
    }
    
    private fun analyzeForStatement(forStmt: PsiForStatement, holder: ProblemsHolder) {
        val body = forStmt.body
        if (body is PsiBlockStatement) {
            val statements = body.codeBlock.statements
            if (statements.size > 20) {
                holder.registerProblem(
                    forStmt,
                    "For loop body is too complex. Consider extracting to a method.",
                    ProblemHighlightType.WARNING
                )
            }
        }
    }
    
    private fun analyzeWhileStatement(whileStmt: PsiWhileStatement, holder: ProblemsHolder) {
        val condition = whileStmt.condition
        if (condition is PsiBinaryExpression && isComplexBooleanExpression(condition)) {
            holder.registerProblem(
                condition,
                "Complex while condition. Consider extracting to a well-named method.",
                ProblemHighlightType.WARNING
            )
        }
    }
    
    private fun analyzeExpression(expression: PsiExpression, holder: ProblemsHolder) {
        when (expression) {
            is PsiBinaryExpression -> analyzeBinaryExpression(expression, holder)
            is PsiMethodCallExpression -> analyzeMethodCall(expression, holder)
        }
    }
    
    private fun analyzeBinaryExpression(binaryExpr: PsiBinaryExpression, holder: ProblemsHolder) {
        // Check for string concatenation in loops
        if (binaryExpr.operationToken.text == "+") {
            val leftType = binaryExpr.lOperand.type
            val rightType = binaryExpr.rOperand.type
            
            if (leftType.equalsToText("java.lang.String") || rightType.equalsToText("java.lang.String")) {
                val parent = binaryExpr.parent
                if (parent is PsiForStatement || parent is PsiWhileStatement) {
                    holder.registerProblem(
                        binaryExpr,
                        "String concatenation in loop. Consider using StringBuilder.",
                        ProblemHighlightType.WARNING
                    )
                }
            }
        }
    }
    
    private fun analyzeMethodCall(methodCall: PsiMethodCallExpression, holder: ProblemsHolder) {
        val method = methodCall.resolveMethod()
        if (method != null) {
            val methodName = method.name
            
            // Check for potentially problematic method calls
            when (methodName) {
                "printStackTrace" -> {
                    holder.registerProblem(
                        methodCall,
                        "Avoid using printStackTrace() in production code. Use proper logging.",
                        ProblemHighlightType.WARNING
                    )
                }
                "System.out.println" -> {
                    holder.registerProblem(
                        methodCall,
                        "Avoid using System.out.println() in production code. Use proper logging.",
                        ProblemHighlightType.WARNING
                    )
                }
            }
        }
    }
    
    private fun isComplexBooleanExpression(expression: PsiBinaryExpression): Boolean {
        var complexity = 0
        PsiTreeUtil.processElements(expression) { element ->
            when (element) {
                is PsiBinaryExpression -> {
                    val op = element.operationToken.text
                    if (op == "&&" || op == "||") {
                        complexity++
                    }
                }
            }
            true
        }
        return complexity > 2
    }
    
    private fun isVariableUsed(variable: PsiVariable): Boolean {
        val name = variable.name ?: return false
        val containingFile = variable.containingFile
        val document = com.intellij.psi.PsiDocumentManager.getInstance(variable.project).getDocument(containingFile)
        
        if (document == null) return false
        
        val text = document.text
        val variableStart = variable.textRange.startOffset
        val variableEnd = variable.textRange.endOffset
        
        // Count occurrences of the variable name (excluding the declaration)
        val beforeDeclaration = text.substring(0, variableStart)
        val afterDeclaration = text.substring(variableEnd)
        
        val occurrencesBefore = beforeDeclaration.split(name).size - 1
        val occurrencesAfter = afterDeclaration.split(name).size - 1
        
        return occurrencesBefore > 0 || occurrencesAfter > 0
    }
}
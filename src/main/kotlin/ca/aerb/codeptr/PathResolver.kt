package ca.aerb.codeptr

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

/**
 * Utility class for resolving file paths based on different reference modes.
 */
object PathResolver {
    
    /**
     * Resolves the file path based on the specified reference mode.
     * 
     * @param project The current project
     * @param virtualFile The virtual file to get the path for
     * @param mode The path reference mode to use
     * @return The resolved file path string
     */
    fun resolvePath(project: Project, virtualFile: VirtualFile, mode: PathReferenceMode): String {
        return when (mode) {
            PathReferenceMode.PROJECT_ROOT -> resolveProjectRootPath(project, virtualFile)
            PathReferenceMode.GIT_ROOT -> resolveGitRootPath(project, virtualFile)
            PathReferenceMode.ABSOLUTE_PATH -> resolveAbsolutePath(virtualFile)
        }
    }
    
    /**
     * Resolves path relative to project root.
     */
    private fun resolveProjectRootPath(project: Project, virtualFile: VirtualFile): String {
        val projectBasePath = project.basePath ?: return virtualFile.name
        val filePath = virtualFile.path
        
        return if (filePath.startsWith(projectBasePath)) {
            filePath.substring(projectBasePath.length + 1)
        } else {
            virtualFile.name
        }
    }
    
    /**
     * Resolves path relative to git repository root.
     * Falls back to project root if git is not available.
     */
    private fun resolveGitRootPath(project: Project, virtualFile: VirtualFile): String {
        return try {
            // Try to find git root using file system traversal
            val gitRoot = findGitRoot(virtualFile.path)
            if (gitRoot != null) {
                val filePath = virtualFile.path
                if (filePath.startsWith(gitRoot)) {
                    filePath.substring(gitRoot.length + 1)
                } else {
                    virtualFile.name
                }
            } else {
                // Fallback to project root if not in a git repository
                resolveProjectRootPath(project, virtualFile)
            }
        } catch (e: Exception) {
            // Fallback to project root on any error
            resolveProjectRootPath(project, virtualFile)
        }
    }
    
    /**
     * Finds the git repository root by traversing up the directory tree.
     */
    private fun findGitRoot(filePath: String): String? {
        var currentDir = File(filePath).parentFile
        while (currentDir != null) {
            if (File(currentDir, ".git").exists()) {
                return currentDir.absolutePath
            }
            currentDir = currentDir.parentFile
        }
        return null
    }
    
    /**
     * Resolves absolute file system path.
     */
    private fun resolveAbsolutePath(virtualFile: VirtualFile): String {
        return virtualFile.path
    }
}
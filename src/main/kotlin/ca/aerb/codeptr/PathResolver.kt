package ca.aerb.codeptr

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

object PathResolver {
  fun resolvePath(
      project: Project,
      virtualFile: VirtualFile,
      mode: PathReferenceMode,
  ): String {
    return when (mode) {
      PathReferenceMode.PROJECT_ROOT -> resolveProjectRootPath(project, virtualFile)
      PathReferenceMode.GIT_ROOT -> resolveGitRootPath(project, virtualFile)
      PathReferenceMode.ABSOLUTE_PATH -> resolveAbsolutePath(virtualFile)
    }
  }

  private fun resolveProjectRootPath(
      project: Project,
      virtualFile: VirtualFile,
  ): String {
    val projectBasePath = project.basePath ?: return virtualFile.name
    val filePath = virtualFile.path

    return if (filePath.startsWith(projectBasePath)) {
      filePath.substring(projectBasePath.length + 1)
    } else {
      virtualFile.name
    }
  }

  private fun resolveGitRootPath(
      project: Project,
      virtualFile: VirtualFile,
  ): String {
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

  private fun resolveAbsolutePath(virtualFile: VirtualFile): String {
    return normalizePathSeparators(virtualFile.path)
  }

  /**
   * Normalizes path separators for cross-platform compatibility. Ensures consistent path format
   * across Windows, macOS, and Linux.
   */
  private fun normalizePathSeparators(path: String): String {
    return if (SystemInfo.isWindows) {
      path.replace('/', '\\')
    } else {
      path.replace('\\', '/')
    }
  }
}

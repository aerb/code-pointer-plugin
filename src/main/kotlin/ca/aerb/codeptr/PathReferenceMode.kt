package ca.aerb.codeptr

/**
 * Enumeration for different path reference modes supported by the plugin.
 */
enum class PathReferenceMode {
    /**
     * Use relative paths from the project root directory.
     * Example: src/main/MyClass.java:5
     */
    PROJECT_ROOT,
    
    /**
     * Use relative paths from the git repository root directory.
     * Example: src/main/MyClass.java:5
     */
    GIT_ROOT,
    
    /**
     * Use absolute file system paths.
     * Example: /Users/username/project/src/main/MyClass.java:5
     */
    ABSOLUTE_PATH
}
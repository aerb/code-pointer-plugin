# Code Pointers Plugin Configuration

This document describes the configuration options available in the Code Pointers plugin.

## Overview

The Code Pointers plugin now supports configurable path reference modes and formatting options. You can access these settings through:

**File > Settings > Tools > Code Pointers** (or **IntelliJ IDEA > Preferences > Tools > Code Pointers** on macOS)

## Configuration Options

### Path Reference Mode

Choose how file paths are referenced in copied code pointers:

#### 1. Project Root (Default)
- **Description**: Uses relative paths from the project root directory
- **Example**: `src/main/MyClass.java:5`
- **Use Case**: Best for sharing references within the same project

#### 2. Git Root
- **Description**: Uses relative paths from the git repository root directory
- **Example**: `src/main/MyClass.java:5`
- **Use Case**: Ideal for sharing references across different projects in the same repository
- **Note**: Falls back to Project Root if not in a git repository

#### 3. Absolute Path
- **Description**: Uses full file system paths
- **Example**: `/Users/username/project/src/main/MyClass.java:5`
- **Use Case**: Useful for external documentation or when you need the full path

### Additional Options

#### Include Column Numbers
- **Default**: Enabled
- **Description**: Includes column positions for more precise references
- **Examples**:
  - With columns: `MyClass.java:5:10-15`
  - Without columns: `MyClass.java:5`

#### Show Preview in Tooltip
- **Default**: Enabled
- **Description**: Shows a preview of the generated code pointer in tooltips
- **Note**: This feature is planned for future implementation

## Reference Format Examples

### Single Line Selection
- **Project Root**: `src/main/MyClass.java:5`
- **Git Root**: `src/main/MyClass.java:5`
- **Absolute Path**: `/Users/username/project/src/main/MyClass.java:5`

### Multi-line Selection
- **Project Root**: `src/main/MyClass.java:5-10`
- **Git Root**: `src/main/MyClass.java:5-10`
- **Absolute Path**: `/Users/username/project/src/main/MyClass.java:5-10`

### With Column Numbers
- **Single line**: `src/main/MyClass.java:5:10-15`
- **Multi-line**: `src/main/MyClass.java:5:10-10:15`

## Technical Implementation

### Settings Storage
- Settings are stored in `code-pointer-settings.xml`
- Automatically persisted across IDE sessions
- Settings are applied immediately when changed

### Path Resolution
- **Project Root**: Uses `project.basePath` to determine relative paths
- **Git Root**: Traverses directory tree to find `.git` folder
- **Absolute Path**: Uses the full `VirtualFile.path`

### Error Handling
- Git Root mode gracefully falls back to Project Root if git is not available
- All path resolution includes error handling for edge cases

## Migration from Previous Versions

The plugin maintains backward compatibility:
- Existing behavior is preserved as the default (Project Root mode)
- No action required for existing users
- New features are opt-in through the settings UI

## Troubleshooting

### Settings Not Saving
- Ensure you have write permissions to the IDE configuration directory
- Try restarting the IDE if settings don't apply immediately

### Git Root Not Working
- Ensure the project is in a git repository
- Check that the `.git` folder exists in the project root
- The plugin will automatically fall back to Project Root mode

### Path Resolution Issues
- For absolute paths, ensure the file system is accessible
- For relative paths, verify the project structure is correct

## Future Enhancements

Planned features include:
- Custom reference format templates
- Integration with issue trackers (JIRA, GitHub, etc.)
- History of copied references
- Support for copying multiple selections
- Enhanced tooltip preview functionality
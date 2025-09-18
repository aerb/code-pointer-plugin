# Copy Selection Reference - IntelliJ Plugin

This IntelliJ plugin copies a file reference with line numbers to the clipboard for selected text. Perfect for sharing code locations with team members or referencing specific lines in documentation.

## Features

- **Simple File Reference**: Copies file path with line numbers (e.g., "src/main/MyFile.java:4-10")
- **Works with Any File Type**: Supports all file types in IntelliJ IDEA
- **Relative Paths**: Uses relative paths from project root for cleaner references
- **Single Line Support**: Shows single line numbers when selection is on one line
- **Multi-line Support**: Shows line ranges when selection spans multiple lines

## Installation

### Development Setup

1. Clone this repository
2. Open the project in IntelliJ IDEA
3. Run `./gradlew buildPlugin` to build the plugin
4. Run `./gradlew runIde` to test the plugin in a sandbox IntelliJ instance

### Production Installation

1. Build the plugin: `./gradlew buildPlugin`
2. Install the generated `.zip` file from `build/distributions/` in IntelliJ IDEA
3. Go to `File > Settings > Plugins > Installed` and enable "Selection Lint Reporter"

## Usage

### Method 1: Action Menu
1. Select any text in your editor
2. Go to `Edit > Copy Selection Reference` in the menu
3. The file reference is automatically copied to your clipboard

### Method 2: Keyboard Shortcut
1. Select any text in your editor
2. Press `Ctrl+Alt+R` (Windows/Linux) or `Cmd+Alt+R` (Mac)
3. The file reference is automatically copied to your clipboard

## Examples

### Single Line Selection
If you select text on line 5 of `src/main/MyClass.java`:
- **Copied to clipboard**: `src/main/MyClass.java:5`

### Multi-line Selection
If you select text from lines 4 to 10 of `src/main/MyClass.java`:
- **Copied to clipboard**: `src/main/MyClass.java:4-10`

### Different File Types
Works with any file type:
- `README.md:15-20`
- `package.json:3`
- `styles.css:25-30`
- `script.js:10-15`

## Plugin Architecture

### Core Components

1. **CopySelectionReferenceAction**: Main action class that handles user interactions and clipboard operations

### Process Flow

1. **Text Selection**: User selects text in the editor
2. **Line Calculation**: Plugin calculates start and end line numbers from selection offsets
3. **Path Resolution**: Determines relative file path from project root
4. **Reference Generation**: Creates file reference string with line numbers
5. **Clipboard Copy**: Copies the reference to system clipboard

## Configuration

The plugin requires no configuration - it works out of the box with any file type and project structure.

## Development

### Building the Plugin

```bash
# Build the plugin
./gradlew buildPlugin

# Run in sandbox IDE
./gradlew runIde

# Run tests
./gradlew test
```

### Project Structure

```
src/main/
├── kotlin/com/example/selectionplugin/
│   └── CopySelectionReferenceAction.kt  # Main action handler
└── resources/META-INF/
    └── plugin.xml                       # Plugin configuration
```

## Compatibility

- **IntelliJ IDEA**: 2023.2 and later
- **Java**: 17+
- **Kotlin**: 1.9.10+
- **Supported Languages**: Java (primary), with extensibility for other languages

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Troubleshooting

### Common Issues

1. **Plugin not appearing in menu**: Ensure the plugin is enabled in Settings > Plugins
2. **Nothing copied to clipboard**: Make sure you have selected text before running the action
3. **Build errors**: Ensure you're using Java 17+ and compatible IntelliJ version

## Future Enhancements

- [ ] Support for different reference formats (absolute paths, git URLs, etc.)
- [ ] Configurable reference format through settings
- [ ] Integration with issue trackers (JIRA, GitHub, etc.)
- [ ] Support for copying multiple selections
- [ ] History of copied references
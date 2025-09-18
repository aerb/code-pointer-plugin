# Selection Lint Reporter - IntelliJ Plugin

This IntelliJ plugin analyzes selected text and reports lint positions, helping developers identify code quality issues in their highlighted code.

## Features

- **Text Selection Analysis**: Select any text in your editor and get instant lint analysis
- **Multiple Issue Types**: Detects code style, performance, and potential bug issues
- **Detailed Reporting**: Shows issue positions, severity levels, and helpful messages
- **PSI Integration**: Uses IntelliJ's PSI (Program Structure Interface) for accurate code analysis
- **Custom Inspections**: Includes custom inspection rules for common code quality issues

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
2. Go to `Edit > Report Lint Positions` in the menu
3. View the analysis results in the popup dialog

### Method 2: Keyboard Shortcut
1. Select any text in your editor
2. Press `Ctrl+Alt+L` (Windows/Linux) or `Cmd+Alt+L` (Mac)
3. View the analysis results

## Supported Issue Types

### Code Style Issues
- **Long Lines**: Lines exceeding 120 characters
- **Trailing Whitespace**: Spaces or tabs at end of lines
- **Long Variable Names**: Variable names longer than 30 characters
- **Long String Literals**: Hardcoded strings longer than 50 characters

### Code Quality Issues
- **Long Methods**: Methods with more than 50 lines
- **Too Many Parameters**: Methods with more than 5 parameters
- **Complex Conditions**: Complex boolean expressions in if/while statements
- **Deep Nesting**: Deeply nested if statements
- **TODO/FIXME Comments**: Identifies pending tasks

### Performance Issues
- **String Concatenation in Loops**: Suggests StringBuilder usage
- **Unnecessary Object Creation**: Identifies redundant object instantiation

### Potential Bugs
- **Null Pointer Risks**: Warns about dangerous null operations
- **Empty Catch Blocks**: Identifies catch blocks without proper handling

## Plugin Architecture

### Core Components

1. **ReportLintPositionsAction**: Main action class that handles user interactions
2. **LintReporter**: Utility class for analyzing and reporting lint issues
3. **SelectionLintInspector**: Custom inspection tool for PSI-based analysis
4. **LintIssue**: Data class representing a lint issue with position and metadata

### Analysis Process

1. **Text Selection**: User selects text in the editor
2. **PSI Analysis**: Plugin uses IntelliJ's PSI to parse the selected code
3. **Pattern Matching**: Applies various lint rules to detect issues
4. **Position Calculation**: Calculates exact positions of issues within the selection
5. **Report Generation**: Creates detailed report with issue types, positions, and suggestions

## Configuration

The plugin can be configured through IntelliJ's inspection settings:

1. Go to `File > Settings > Editor > Inspections`
2. Navigate to `Java > Code Style Issues > Selection Lint Analysis`
3. Configure severity levels and enable/disable specific rules

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
│   ├── ReportLintPositionsAction.kt    # Main action handler
│   ├── LintReporter.kt                 # Analysis and reporting logic
│   └── SelectionLintInspector.kt       # Custom inspection tool
└── resources/META-INF/
    └── plugin.xml                      # Plugin configuration
```

### Adding New Lint Rules

To add new lint rules:

1. Extend the analysis methods in `LintReporter.kt`
2. Add new issue detection logic
3. Update the `LintIssue` data class if needed
4. Test with various code samples

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
2. **No analysis results**: Make sure you have selected text before running the analysis
3. **Build errors**: Ensure you're using Java 17+ and compatible IntelliJ version

### Debug Mode

To enable debug logging:

1. Go to `Help > Diagnostic Tools > Debug Log Settings`
2. Add logger: `com.example.selectionplugin`
3. Set level to `DEBUG`
4. Restart IntelliJ IDEA

## Future Enhancements

- [ ] Support for more programming languages (Kotlin, Python, JavaScript)
- [ ] Configurable lint rules through settings
- [ ] Integration with external linters (ESLint, Pylint, etc.)
- [ ] Real-time analysis as you type
- [ ] Export analysis results to various formats
- [ ] Team-wide lint rule sharing
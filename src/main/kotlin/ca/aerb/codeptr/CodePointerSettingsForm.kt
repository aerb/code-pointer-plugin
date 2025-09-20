package ca.aerb.codeptr

import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

/**
 * Settings form UI for the Code Pointer plugin.
 * Provides a user-friendly interface for configuring plugin settings.
 */
class CodePointerSettingsForm {
    
    val rootPanel: JPanel = JPanel(BorderLayout())
    
    private val pathReferenceModeComboBox: JComboBox<PathReferenceMode>
    private val includeColumnNumbersCheckBox: JCheckBox
    private val showPreviewInTooltipCheckBox: JCheckBox
    
    init {
        setupUI()
        
        pathReferenceModeComboBox = JComboBox(PathReferenceMode.values())
        includeColumnNumbersCheckBox = JCheckBox("Include column numbers in references")
        showPreviewInTooltipCheckBox = JCheckBox("Show preview in tooltip")
        
        setupComponents()
    }
    
    private fun setupUI() {
        val mainPanel = JPanel(GridBagLayout())
        val constraints = GridBagConstraints()
        
        // Path Reference Mode
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.anchor = GridBagConstraints.WEST
        constraints.insets = java.awt.Insets(5, 5, 5, 5)
        mainPanel.add(JLabel("Path Reference Mode:"), constraints)
        
        constraints.gridx = 1
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.weightx = 1.0
        mainPanel.add(JPanel(), constraints) // Spacer
        
        // Column Numbers Option
        constraints.gridx = 0
        constraints.gridy = 1
        constraints.gridwidth = 2
        constraints.fill = GridBagConstraints.NONE
        constraints.weightx = 0.0
        
        // Tooltip Preview Option
        constraints.gridy = 2
        
        rootPanel.add(mainPanel, BorderLayout.NORTH)
        
        // Add description panel
        val descriptionPanel = createDescriptionPanel()
        rootPanel.add(descriptionPanel, BorderLayout.CENTER)
    }
    
    private fun setupComponents() {
        val mainPanel = rootPanel.getComponent(0) as JPanel
        val constraints = GridBagConstraints()
        
        // Add path reference mode combo box
        constraints.gridx = 1
        constraints.gridy = 0
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.weightx = 1.0
        constraints.insets = java.awt.Insets(5, 5, 5, 5)
        mainPanel.add(pathReferenceModeComboBox, constraints)
        
        // Add checkboxes
        constraints.gridx = 0
        constraints.gridy = 1
        constraints.gridwidth = 2
        constraints.fill = GridBagConstraints.NONE
        constraints.weightx = 0.0
        mainPanel.add(includeColumnNumbersCheckBox, constraints)
        
        constraints.gridy = 2
        mainPanel.add(showPreviewInTooltipCheckBox, constraints)
        
        // Add tooltips
        pathReferenceModeComboBox.toolTipText = "Choose how file paths are referenced in copied code pointers"
        includeColumnNumbersCheckBox.toolTipText = "Include column numbers for more precise references (e.g., file.java:5:10)"
        showPreviewInTooltipCheckBox.toolTipText = "Show a preview of the code pointer in tooltips"
    }
    
    private fun createDescriptionPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        val textArea = JTextArea().apply {
            isEditable = false
            isOpaque = false
            text = """
                Path Reference Modes:
                
                • Project Root: Uses relative paths from the project root directory
                  Example: src/main/MyClass.java:5
                
                • Git Root: Uses relative paths from the git repository root
                  Example: src/main/MyClass.java:5
                
                • Absolute Path: Uses full file system paths
                  Example: /Users/username/project/src/main/MyClass.java:5
                
                Column Numbers: When enabled, includes column positions for more precise references.
                Tooltip Preview: Shows a preview of the generated code pointer in tooltips.
            """.trimIndent()
            font = font.deriveFont(font.size - 1f)
        }
        
        val scrollPane = JScrollPane(textArea).apply {
            border = BorderFactory.createTitledBorder("Reference Format Examples")
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        }
        
        panel.add(scrollPane, BorderLayout.CENTER)
        return panel
    }
    
    // Getters and setters for form data
    fun getPathReferenceMode(): PathReferenceMode = pathReferenceModeComboBox.selectedItem as PathReferenceMode
    fun setPathReferenceMode(mode: PathReferenceMode) { pathReferenceModeComboBox.selectedItem = mode }
    
    fun getIncludeColumnNumbers(): Boolean = includeColumnNumbersCheckBox.isSelected
    fun setIncludeColumnNumbers(include: Boolean) { includeColumnNumbersCheckBox.isSelected = include }
    
    fun getShowPreviewInTooltip(): Boolean = showPreviewInTooltipCheckBox.isSelected
    fun setShowPreviewInTooltip(show: Boolean) { showPreviewInTooltipCheckBox.isSelected = show }
}
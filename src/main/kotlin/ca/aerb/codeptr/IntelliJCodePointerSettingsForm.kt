package ca.aerb.codeptr

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

/**
 * Settings form using IntelliJ's built-in components and FormBuilder. This is the recommended
 * approach for IntelliJ plugins.
 */
class IntelliJCodePointerSettingsForm {
  // Initialize components
  private val pathReferenceModeComboBox = ComboBox(PathReferenceMode.entries.toTypedArray())
  private val includeColumnNumbersCheckBox = JBCheckBox("Include column numbers")
  private val showPreviewInTooltipCheckBox = JBCheckBox("Show code preview in tooltips")

  val rootPanel: JPanel =
      FormBuilder.createFormBuilder()
          .addLabeledComponent(JBLabel("File path format:"), pathReferenceModeComboBox)
          .addComponent(includeColumnNumbersCheckBox)
          .addComponent(showPreviewInTooltipCheckBox)
          .addComponentFillVertically(JPanel(), 0)
          .panel

  var pathReferenceMode: PathReferenceMode
    get() = pathReferenceModeComboBox.selectedItem as PathReferenceMode
    set(mode) {
      pathReferenceModeComboBox.selectedItem = mode
    }

  var includeColumnNumbers: Boolean
    get() = includeColumnNumbersCheckBox.isSelected
    set(include) {
      includeColumnNumbersCheckBox.isSelected = include
    }

  var showPreviewInTooltip: Boolean
    get() = showPreviewInTooltipCheckBox.isSelected
    set(show) {
      showPreviewInTooltipCheckBox.isSelected = show
    }
}

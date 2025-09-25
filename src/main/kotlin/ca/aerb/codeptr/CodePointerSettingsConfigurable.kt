package ca.aerb.codeptr

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class CodePointerSettingsConfigurable : Configurable {
  private var settingsForm: CodePointerSettingsForm? = null

  override fun getDisplayName(): String = "Code Pointers"

  override fun createComponent(): JComponent? {
    settingsForm = CodePointerSettingsForm()
    return settingsForm?.rootPanel
  }

  override fun isModified(): Boolean {
    val settings = CodePointerSettingsState.getInstance()
    val form = settingsForm ?: return false

    return form.getPathReferenceMode() != settings.getPathReferenceMode() ||
        form.getIncludeColumnNumbers() != settings.getIncludeColumnNumbers() ||
        form.getShowPreviewInTooltip() != settings.getShowPreviewInTooltip()
  }

  override fun apply() {
    val settings = CodePointerSettingsState.getInstance()
    val form = settingsForm ?: return

    settings.setPathReferenceMode(form.getPathReferenceMode())
    settings.setIncludeColumnNumbers(form.getIncludeColumnNumbers())
    settings.setShowPreviewInTooltip(form.getShowPreviewInTooltip())
  }

  override fun reset() {
    val settings = CodePointerSettingsState.getInstance()
    val form = settingsForm ?: return

    form.setPathReferenceMode(settings.getPathReferenceMode())
    form.setIncludeColumnNumbers(settings.getIncludeColumnNumbers())
    form.setShowPreviewInTooltip(settings.getShowPreviewInTooltip())
  }

  override fun disposeUIResources() {
    settingsForm = null
  }
}

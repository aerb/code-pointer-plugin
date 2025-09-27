package ca.aerb.codeptr

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class CodePointerSettingsConfigurable : Configurable {
  private lateinit var form: IntelliJCodePointerSettingsForm

  override fun getDisplayName(): String = "Code Pointers"

  override fun createComponent(): JComponent {
    form = IntelliJCodePointerSettingsForm()
    return form.rootPanel
  }

  override fun isModified(): Boolean {
    val settings = CodePointerSettingsState.getInstance()

    return form.pathReferenceMode != settings.pathReferenceMode ||
        form.includeColumnNumbers != settings.includeColumnNumbers ||
        form.showPreviewInTooltip != settings.showPreviewInTooltip
  }

  override fun apply() {
    val settings = CodePointerSettingsState.getInstance()

    settings.pathReferenceMode = form.pathReferenceMode
    settings.includeColumnNumbers = form.includeColumnNumbers
    settings.showPreviewInTooltip = form.showPreviewInTooltip
  }

  override fun reset() {
    val settings = CodePointerSettingsState.getInstance()

    form.pathReferenceMode = settings.pathReferenceMode
    form.includeColumnNumbers = settings.includeColumnNumbers
    form.showPreviewInTooltip = settings.showPreviewInTooltip
  }

  override fun disposeUIResources() {}
}

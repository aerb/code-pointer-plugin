package ca.aerb.codeptr

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*

/**
 * Persistent settings state for the Code Pointer plugin. Stores user preferences for path reference
 * modes and other configuration options.
 */
@State(
  name = "CodePointerSettings",
  storages = [Storage("code-pointer-settings.xml")],
)
@Service
class CodePointerSettingsState : PersistentStateComponent<CodePointerSettingsState.State> {
  data class State(
    var pathReferenceMode: PathReferenceMode = PathReferenceMode.PROJECT_ROOT,
    var includeColumnNumbers: Boolean = true,
    var showPreviewInTooltip: Boolean = true,
  )

  private var state = State()

  override fun getState(): State = state

  override fun loadState(state: State) {
    this.state = state
  }

  companion object {
    fun getInstance(): CodePointerSettingsState {
      return ApplicationManager.getApplication().getService(CodePointerSettingsState::class.java)
    }
  }

  var pathReferenceMode: PathReferenceMode
    get() = state.pathReferenceMode
    set(mode) {
      state.pathReferenceMode = mode
    }

  var includeColumnNumbers: Boolean
    get() = state.includeColumnNumbers
    set(include) {
      state.includeColumnNumbers = include
    }

  var showPreviewInTooltip: Boolean
    get() = state.showPreviewInTooltip
    set(show) {
      state.showPreviewInTooltip = show
    }
}

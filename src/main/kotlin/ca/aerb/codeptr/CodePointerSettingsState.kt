package ca.aerb.codeptr

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*

/**
 * Persistent settings state for the Code Pointer plugin.
 * Stores user preferences for path reference modes and other configuration options.
 */
@State(
    name = "CodePointerSettings",
    storages = [Storage("code-pointer-settings.xml")]
)
@Service
class CodePointerSettingsState : PersistentStateComponent<CodePointerSettingsState.State> {
    
    data class State(
        var pathReferenceMode: PathReferenceMode = PathReferenceMode.PROJECT_ROOT,
        var includeColumnNumbers: Boolean = true,
        var showPreviewInTooltip: Boolean = true
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
    
    // Convenience methods for accessing settings
    fun getPathReferenceMode(): PathReferenceMode = state.pathReferenceMode
    fun setPathReferenceMode(mode: PathReferenceMode) { state.pathReferenceMode = mode }
    
    fun getIncludeColumnNumbers(): Boolean = state.includeColumnNumbers
    fun setIncludeColumnNumbers(include: Boolean) { state.includeColumnNumbers = include }
    
    fun getShowPreviewInTooltip(): Boolean = state.showPreviewInTooltip
    fun setShowPreviewInTooltip(show: Boolean) { state.showPreviewInTooltip = show }
}
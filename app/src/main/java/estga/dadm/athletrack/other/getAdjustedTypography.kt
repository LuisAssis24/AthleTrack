package estga.dadm.athletrack.other

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import estga.dadm.athletrack.ui.theme.Typography

@Composable
fun getAdjustedTypography(): TextStyle {
    val fontScale = LocalConfiguration.current.fontScale
    return if (fontScale > 1.0f) {
        Typography.labelSmall // Usa uma tipografia menor para evitar clipping.
    } else {
        Typography.bodyMedium // Usa a tipografia padrão.
    }
}
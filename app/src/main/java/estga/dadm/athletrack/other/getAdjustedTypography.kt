package estga.dadm.athletrack.other

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import estga.dadm.athletrack.ui.theme.Typography

/**
 * Função que ajusta a tipografia com base na escala de fonte configurada no dispositivo.
 *
 * @return Retorna um estilo de texto ajustado. Se a escala de fonte for maior que 1.0,
 * utiliza `Typography.labelSmall` para evitar clipping. Caso contrário, utiliza `Typography.bodyMedium`.
 */
@Composable
fun getAdjustedTypography(): TextStyle {
    val fontScale = LocalConfiguration.current.fontScale
    return if (fontScale > 1.0f) {
        Typography.labelSmall // Usa uma tipografia menor para evitar clipping.
    } else {
        Typography.bodyMedium // Usa a tipografia padrão.
    }
}
package estga.dadm.athletrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = darkColorScheme(
    primary = White,
    secondary = Gray,
    onPrimary = BlueAccent,

    primaryContainer = CardBlue,
    surface = BackgroundBlueDark,

    error = RedError,
)

@Composable
fun AthleTrackTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}

package estga.dadm.athletrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = BlueAccent,
    background = BluePrimary,
    surface = BluePrimary,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    error = RedError,
    onError = White
)

@Composable
fun AthleTrackTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}

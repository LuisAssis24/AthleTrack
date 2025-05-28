package estga.dadm.athletrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Define o esquema de cores para o tema escuro do aplicativo AthleTrack.
 *
 * As cores incluem:
 * - `primary`: Cor principal utilizada em elementos destacados.
 * - `inversePrimary`: Cor inversa da principal, usada em textos ou ícones sobre a cor principal.
 * - `secondary`: Cor secundária utilizada em elementos menos destacados.
 * - `onPrimary`: Cor utilizada em textos ou ícones sobre a cor principal.
 * - `tertiary`: Cor terciária, definida como transparente.
 * - `primaryContainer`: Cor utilizada em contêineres ou cartões.
 * - `surface`: Cor de fundo principal da interface.
 * - `error`: Cor utilizada para indicar erros.
 */
private val ColorScheme = darkColorScheme(
    primary = White,
    inversePrimary = Black,
    secondary = Gray,
    onPrimary = BlueAccent,
    tertiary = Transparent,

    primaryContainer = CardBlue,
    surface = BackgroundBlueDark,
    error = RedError,
)

/**
 * Função composable que aplica o tema AthleTrack ao conteúdo fornecido.
 *
 * @param content Conteúdo da interface do usuário que será estilizado com o tema AthleTrack.
 */
@Composable
fun AthleTrackTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}
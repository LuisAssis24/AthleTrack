package estga.dadm.athletrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import estga.dadm.athletrack.navigation.AthleTrackNavGraph
import estga.dadm.athletrack.ui.theme.AthleTrackTheme

/**
 * Classe principal da aplicação que representa a atividade inicial.
 * Responsável por configurar o tema, a navegação e o layout principal.
 */
class MainActivity : ComponentActivity() {

    /**
     * Métrodo chamado quando a atividade é criada.
     * Configura o layout da interface do usuário usando Jetpack Compose.
     *
     * @param savedInstanceState Estado salvo da atividade, usado para restaurar dados em recriações.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita o modo de borda a borda para a atividade.
        enableEdgeToEdge()

        // Define o conteúdo da atividade usando Jetpack Compose.
        setContent {
            // Aplica o tema personalizado da aplicação.
            AthleTrackTheme {
                // Cria um controlador de navegação para gerenciar a navegação entre telas.
                val navController = rememberNavController()

                // Define a superfície principal que ocupa o espaço disponível.
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Configura o gráfico de navegação da aplicação.
                    AthleTrackNavGraph(navController = navController)
                }
            }
        }
    }
}
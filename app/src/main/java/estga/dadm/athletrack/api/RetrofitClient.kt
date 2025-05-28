package estga.dadm.athletrack.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto responsável por configurar e fornecer instâncias de serviços Retrofit
 * para comunicação com a API do AthleTrack.
 */
object RetrofitClient {
    /**
     * URL base para a API.
     * - `http://10.0.2.2:8080/api/`: Usada para testes locais no emulador.
     * - `https://athletrack-backend.onrender.com`: Usada para produção.
     */
    //private const val BASE_URL = "http://10.0.2.2:8080/api/" // Emulador Local
    private const val BASE_URL = "https://athletrack-backend.onrender.com" // Produção

    /**
     * Instância do Retrofit configurada com a URL base e o conversor Gson.
     * Criada de forma lazy para inicialização apenas quando necessário.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Serviço para operações relacionadas ao login de usurious.
     * Criado a partir da interface `UserService`.
     */
    val loginService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    /**
     * Serviço para operações relacionadas aos treinos.
     * Criado a partir da interface `TreinosService`.
     */
    val treinosService: TreinosService by lazy {
        retrofit.create(TreinosService::class.java)
    }

    /**
     * Serviço para operações relacionadas às presenças.
     * Criado a partir da interface `PresencasService`.
     */
    val presencasService: PresencasService by lazy {
        retrofit.create(PresencasService::class.java)
    }

    /**
     * Serviço para operações relacionadas aos eventos.
     * Criado a partir da interface `EventosService`.
     */
    val eventosService: EventosService by lazy {
        retrofit.create(EventosService::class.java)
    }

    /**
     * Serviço para operações relacionadas às modalidades.
     * Criado a partir da interface `ModalidadesService`.
     */
    val modalidadesService: ModalidadesService by lazy {
        retrofit.create(ModalidadesService::class.java)
    }
}
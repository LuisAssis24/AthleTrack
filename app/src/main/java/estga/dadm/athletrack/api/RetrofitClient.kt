package estga.dadm.athletrack.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Configura o cliente Retrofit para comunicação com a API.
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/" // Emulador Local
    //private const val BASE_URL = "https://athletrack-backend.onrender.com" // Produção

    // Instância do Retrofit configurada com a URL base e o conversor Gson.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Serviços disponíveis no cliente Retrofit.
    val loginService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val treinosService: TreinosService by lazy {
        retrofit.create(TreinosService::class.java)
    }

    val presencasService: PresencasService by lazy {
        retrofit.create(PresencasService::class.java)
    }

    val eventosService: EventosService by lazy {
        retrofit.create(EventosService::class.java)
    }

    val modalidadesService: ModalidadesService by lazy {
        retrofit.create(ModalidadesService::class.java)
    }
}
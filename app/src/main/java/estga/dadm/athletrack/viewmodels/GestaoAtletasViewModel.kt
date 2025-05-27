package estga.dadm.athletrack.viewmodels

    import androidx.lifecycle.*
    import estga.dadm.athletrack.api.*
    import kotlinx.coroutines.flow.*
    import kotlinx.coroutines.launch
    import okhttp3.ResponseBody

    /**
     * ViewModel responsável pela gestão de atletas e modalidades.
     */
    class GestaoAtletasViewModel : ViewModel() {

        // Estado interno que armazena a lista de atletas.
        private val _atletas = MutableStateFlow<List<User>>(emptyList())

        /**
         * Estado público que expõe a lista de atletas para observação.
         */
        val atletas: StateFlow<List<User>> = _atletas

        // Estado interno que armazena a lista de modalidades.
        private val _modalidades = MutableStateFlow<List<Modalidade>>(emptyList())

        /**
         * Estado público que expõe a lista de modalidades para observação.
         */
        val modalidades: StateFlow<List<Modalidade>> = _modalidades

        /**
         * Carrega a lista de atletas do serviço remoto.
         * Filtra apenas os usuários do tipo "atleta".
         */
        fun carregarAtletas() {
            viewModelScope.launch {
                try {
                    val todos = RetrofitClient.loginService.listar()
                    _atletas.value = todos.filter { it.tipo.lowercase() == "atleta" }
                } catch (e: Exception) {
                    _atletas.value = emptyList()
                }
            }
        }

        /**
         * Cria um novo atleta no sistema.
         *
         * @param request Objeto contendo os dados do atleta a ser criado.
         * @param callback Callback a ser executado após a criação, indicando sucesso ou erro.
         */
        fun criarAtleta(request: UserCreate, callback: (Boolean, String) -> Unit) {
            viewModelScope.launch {
                try {
                    val resposta = RetrofitClient.loginService.criar(request)
                    carregarAtletas()

                    // Buscar o último utilizador criado pelo nome.
                    val todos = RetrofitClient.loginService.listar()
                    val novoUser = todos.lastOrNull { it.nome == request.nome && it.tipo == "atleta" }
                    if (novoUser != null) {
                        callback(true, "Utilizador criado!!\nID = ${novoUser.idSocio}")
                    } else {
                        callback(true, resposta.toString())
                    }
                } catch (e: Exception) {
                    callback(false, "Erro ao criar atleta: ${e.message}")
                }
            }
        }

        /**
         * Apaga um atleta do sistema, verificando a senha do professor.
         *
         * @param idAtleta ID do atleta a ser apagado.
         * @param idProfessor ID do professor que está realizando a ação.
         * @param senha Senha do professor para autenticação.
         * @param callback Callback a ser executado após a exclusão, indicando sucesso ou erro.
         */
        fun apagarAtletaComSenha(
            idAtleta: Int,
            idProfessor: Int,
            senha: String,
            callback: (Boolean, String) -> Unit
        ) {
            viewModelScope.launch {
                try {
                    val login = UserRequest(idProfessor, senha)
                    val resposta: ResponseBody = RetrofitClient.loginService.eliminar(idAtleta, login)
                    carregarAtletas()
                    callback(true, resposta.string())
                } catch (e: Exception) {
                    callback(false, "Erro ao apagar atleta: ${e.message}")
                }
            }
        }

        /**
         * Carrega a lista de modalidades disponíveis do serviço remoto.
         */
        fun carregarModalidades() {
            viewModelScope.launch {
                try {
                    val response = RetrofitClient.modalidadesService.listarModalidades()
                    _modalidades.value = response
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        /**
         * Carrega modalidades hardcoded para testes ou desenvolvimento.
         *
         * @param modalidades Lista de modalidades a serem carregadas.
         */
        fun carregarAtletasHardcoded(atletas: List<User>) {
            _atletas.value = atletas
        }

        /**
         * Cria um atleta hardcoded para testes ou desenvolvimento.
         *
         * @param request Dados do atleta a ser criado.
         * @param simularErro Se verdadeiro, simula um erro na criação.
         * @param callback Callback que recebe o resultado da operação.
         */
        fun criarAtletaHardcoded(
            request: UserCreate,
            simularErro: Boolean = false,
            callback: (Boolean, String) -> Unit
        ) {
            if (simularErro) {
                callback(false, "Erro ao criar atleta.")
            } else {
                _atletas.value = _atletas.value + User(3, request.nome, request.tipo)
                callback(true, "Atleta criado com sucesso!")
            }
        }

        /**
         * Apaga um atleta hardcoded para testes ou desenvolvimento.
         *
         * @param idAtleta ID do atleta a ser apagado.
         * @param simularErro Se verdadeiro, simula um erro na exclusão.
         * @param callback Callback que recebe o resultado da operação.
         */
        fun apagarAtletaHardcoded(idAtleta: Int, simularErro: Boolean = false, callback: (Boolean, String) -> Unit) {
            if (simularErro) {
                callback(false, "Erro ao apagar atleta.")
            } else {
                _atletas.value = _atletas.value.filter { it.idSocio != idAtleta }
                callback(true, "Atleta apagado com sucesso!")
            }
        }

        /**
         * Carrega modalidades hardcoded para testes ou desenvolvimento.
         * Substitui o estado interno `_modalidades` com a lista fornecida.
         *
         * @param modalidades Lista de modalidades a serem carregadas.
         */
        fun carregarModalidadesHardcoded(modalidades: List<Modalidade>) {
            _modalidades.value = modalidades
        }

    }
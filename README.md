# AthleTrack

Projeto temático desenvolvido no âmbito da unidade curricular de **Desenvolvimento de Aplicações Móveis**, que permite a gestão de treinos, atletas e eventos desportivos com suporte a presenças via QR code.

## Sobre a Aplicação

A aplicação possui duas áreas principais:
- **Área do Professor**, onde é possível:
    - Gerir treinos e atletas;
    - Criar eventos no calendário;
    - Gerir presenças, incluindo leitura via QR code;
    - Fazer login/logout com persistência de sessão.

- **Área do Atleta**, onde o aluno pode:
    - Consultar os treinos do dia;
    - Registar a sua presença através da leitura de um QR code;
    - Consultar eventos futuros no calendário.

---

## Pré-requisitos

- Android Studio Arctic Fox ou superior;
- SDK 33+;
- Emulador Android configurado ou dispositivo físico;
- Gradle (versão mínima: 8.0).

---

## Atenção

**Antes de fazer o build do APK**, é necessário:

- **Remover a pasta `backend`** de dentro do diretório principal do projeto.
    - A presença desta pasta pode interferir com o Gradle Sync do Android Studio.
    - O `backend` é um projeto Spring Boot independente e deve ser executado separadamente se necessário.

---

## Como correr a aplicação

1. Clonar ou copiar o projeto.
2. Abrir o projeto no Android Studio.
3. Eliminar a pasta `backend/` do projeto (caso esteja presente).
4. Aguardar que o Gradle Sync conclua com sucesso.
5. Fazer **Build > Build APK(s)** ou executar em dispositivo/emulador.

---

## Estrutura do Projeto

- `/app/` - Código-fonte da aplicação Android;
- `/backend/` *(fora do projeto Android)* - Código-fonte do backend (Spring Boot);
- `/screens/` - Composables de UI principais;
- `/viewmodels/` - Lógica de negócio;
- `/components/` - Componentes reutilizáveis;
- `/api/` - DTOs e interfaces de comunicação com o backend via Retrofit.

---

## Autores

- ESTGA - Universidade de Aveiro
- Curso: Tecnologias da Informação

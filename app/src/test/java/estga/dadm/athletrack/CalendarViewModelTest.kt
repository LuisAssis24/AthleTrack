package estga.dadm.athletrack

import estga.dadm.athletrack.api.Evento
import estga.dadm.athletrack.viewmodels.CalendarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {

    private lateinit var viewModel: CalendarViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CalendarViewModel()
    }

    @Test
    fun `estado inicial deve ser correto`() = runTest {
        assertEquals(LocalDate.now(), viewModel.selectedDate.first())
        assertEquals(YearMonth.now(), viewModel.currentMonth.first())
        assertEquals(0, viewModel.eventos.first().size)
    }

    @Test
    fun `selecionarData deve atualizar a data selecionada`() = runTest {
        val novaData = LocalDate.of(2023, 12, 25)
        viewModel.selecionarData(novaData)
        assertEquals(novaData, viewModel.selectedDate.first())
    }

    @Test
    fun `irParaMesAnterior deve atualizar o mês corretamente`() = runTest {
        val mesAtual = viewModel.currentMonth.first()
        viewModel.irParaMesAnterior()
        assertEquals(mesAtual.minusMonths(1), viewModel.currentMonth.first())
    }

    @Test
    fun `irParaMesSeguinte deve atualizar o mês corretamente`() = runTest {
        val mesAtual = viewModel.currentMonth.first()
        viewModel.irParaMesSeguinte()
        assertEquals(mesAtual.plusMonths(1), viewModel.currentMonth.first())
    }

    @Test
    fun `carregarEventosHardcoded deve carregar eventos corretamente`() = runTest {
        val eventosMock = listOf(
            Evento(
                localEvento = "Ginásio",
                data = "2023-12-25",
                hora = "10:00",
                descricao = "Treino de Natal"
            ),
            Evento(
                localEvento = "Campo",
                data = "2023-12-26",
                hora = "15:00",
                descricao = "Treino ao ar livre"
            )
        )
        viewModel.carregarEventosHardcoded(eventosMock)
        val eventos = viewModel.eventos.first()
        assertEquals(2, eventos.size)
        assertEquals("Ginásio", eventos[0].localEvento)
        assertEquals("Treino de Natal", eventos[0].descricao)
    }

    @Test
    fun `carregarEventosHardcoded deve retornar lista vazia quando nenhum evento for carregado`() =
        runTest {
            viewModel.carregarEventosHardcoded(emptyList())
            val eventos = viewModel.eventos.first()
            assertEquals(0, eventos.size)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
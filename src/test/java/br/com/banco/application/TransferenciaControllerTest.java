package br.com.banco.application;

import br.com.banco.application.dtos.TransferenciaResponse;
import br.com.banco.domain.models.Conta;
import br.com.banco.domain.models.TipoTransacao;
import br.com.banco.domain.models.Transferencia;
import br.com.banco.domain.repositories.ContaRepository;
import br.com.banco.domain.repositories.TransferenciaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
class TransferenciaControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private MockMvc mockMvc;

    private Conta conta;

    @BeforeEach
    void setUp() {
        transferenciaRepository.deleteAll();
        contaRepository.deleteAll();
        this.conta = new Conta("Luis Felipe");
        contaRepository.save(conta);
    }

    @Test
    @DisplayName("deve listar transferencias")
    void test1() throws Exception {

        Transferencia transferencia1 = new Transferencia(LocalDate.now(),
                new BigDecimal(25),
                TipoTransacao.SAQUE,
                "Luis Felipe",
                conta.getIdConta());

        Transferencia transferencia2 = new Transferencia(LocalDate.now(),
                new BigDecimal(50),
                TipoTransacao.SAQUE,
                "Felipe Luis",
                conta.getIdConta());

        transferenciaRepository.saveAll(List.of(transferencia1, transferencia2));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br");

        String payload = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = objectMapper.getTypeFactory();

        List<TransferenciaResponse> response = objectMapper
                .readValue(payload, typeFactory.constructCollectionType(List.class, TransferenciaResponse.class));

        assertThat(response)
                .hasSize(2)
                .extracting("dataTransferencia", "valor", "tipo", "nomeOperadorTransacao", "contaId")
                .contains(
                        new Tuple(transferencia1.getDataTransferencia(),
                                transferencia1.getValor().setScale(2, RoundingMode.UNNECESSARY),
                                transferencia1.getTipo(),
                                transferencia1.getNomeOperadorTransacao(),
                                transferencia1.getContaId()),

                        new Tuple(transferencia2.getDataTransferencia(),
                                transferencia2.getValor().setScale(2, RoundingMode.UNNECESSARY),
                                transferencia2.getTipo(),
                                transferencia2.getNomeOperadorTransacao(),
                                transferencia2.getContaId()));
    }

    @Test
    @DisplayName("deve listar transferencias realizadas por uma pessoa")
    void test2() throws Exception {

        Transferencia transferencia1 = new Transferencia(LocalDate.now(),
                new BigDecimal(25),
                TipoTransacao.SAQUE,
                "Luis",
                conta.getIdConta());

        Transferencia transferencia2 = new Transferencia(LocalDate.now(),
                new BigDecimal(1000),
                TipoTransacao.SAQUE,
                "Luis",
                conta.getIdConta());

        Transferencia transferencia3 = new Transferencia(LocalDate.now(),
                new BigDecimal(500),
                TipoTransacao.SAQUE,
                "Joaquim",
                conta.getIdConta());

        transferenciaRepository.saveAll(List.of(transferencia1, transferencia2, transferencia3));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/transferencias?nome=Luis")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br");

        String payload = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = objectMapper.getTypeFactory();

        List<TransferenciaResponse> response = objectMapper
                .readValue(payload, typeFactory.constructCollectionType(List.class, TransferenciaResponse.class));

        assertThat(response)
                .hasSize(2)
                .extracting("dataTransferencia", "valor", "tipo", "nomeOperadorTransacao", "contaId")
                .contains(
                        new Tuple(transferencia1.getDataTransferencia(),
                                transferencia1.getValor().setScale(2, RoundingMode.UNNECESSARY),
                                transferencia1.getTipo(),
                                transferencia1.getNomeOperadorTransacao(),
                                transferencia1.getContaId()),

                        new Tuple(transferencia2.getDataTransferencia(),
                                transferencia2.getValor().setScale(2, RoundingMode.UNNECESSARY),
                                transferencia2.getTipo(),
                                transferencia2.getNomeOperadorTransacao(),
                                transferencia2.getContaId()));
    }

    @Test
    @DisplayName("deve listar transferencias realizadas entre um período")
    void test3() throws Exception {

        Transferencia transferencia1 = new Transferencia(LocalDate.of(2015, 5, 22),
                new BigDecimal(25),
                TipoTransacao.SAQUE,
                "Luis",
                conta.getIdConta());

        Transferencia transferencia2 = new Transferencia(LocalDate.of(2015, 8, 30),
                new BigDecimal(1000),
                TipoTransacao.SAQUE,
                "Luis",
                conta.getIdConta());

        Transferencia transferencia3 = new Transferencia(LocalDate.of(2023, 2, 20),
                new BigDecimal(500),
                TipoTransacao.SAQUE,
                "Joaquim",
                conta.getIdConta());

        Transferencia transferencia4 = new Transferencia(LocalDate.of(2023, 5, 13),
                new BigDecimal(500),
                TipoTransacao.SAQUE,
                "Joaquim",
                conta.getIdConta());

        transferenciaRepository.saveAll(List.of(transferencia1, transferencia2, transferencia3, transferencia4));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/transferencias?inicio=30/08/2015&termino=20/02/2023")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br");

        String payload = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = objectMapper.getTypeFactory();

        List<TransferenciaResponse> response = objectMapper
                .readValue(payload, typeFactory.constructCollectionType(List.class, TransferenciaResponse.class));

        assertThat(response)
                .hasSize(2)
                .extracting("dataTransferencia", "valor", "tipo", "nomeOperadorTransacao", "contaId")
                .contains(
                        new Tuple(transferencia2.getDataTransferencia(),
                                transferencia2.getValor().setScale(2, RoundingMode.UNNECESSARY),
                                transferencia2.getTipo(),
                                transferencia2.getNomeOperadorTransacao(),
                                transferencia2.getContaId()),

                        new Tuple(transferencia3.getDataTransferencia(),
                                transferencia3.getValor().setScale(2, RoundingMode.UNNECESSARY),
                                transferencia3.getTipo(),
                                transferencia3.getNomeOperadorTransacao(),
                                transferencia3.getContaId()));
    }

    @Test
    @DisplayName("deve listar transferencias realizadas entre um período e por determinada pessoa")
    void test4() throws Exception {

        Transferencia transferencia1 = new Transferencia(LocalDate.of(2015, 5, 22),
                new BigDecimal(25),
                TipoTransacao.SAQUE,
                "Luis",
                conta.getIdConta());

        Transferencia transferencia2 = new Transferencia(LocalDate.of(2015, 8, 30),
                new BigDecimal(1000),
                TipoTransacao.SAQUE,
                "Luis",
                conta.getIdConta());

        Transferencia transferencia3 = new Transferencia(LocalDate.of(2023, 2, 20),
                new BigDecimal(500),
                TipoTransacao.SAQUE,
                "Joaquim",
                conta.getIdConta());

        Transferencia transferencia4 = new Transferencia(LocalDate.of(2023, 5, 13),
                new BigDecimal(500),
                TipoTransacao.SAQUE,
                "Joaquim",
                conta.getIdConta());

        transferenciaRepository.saveAll(List.of(transferencia1, transferencia2, transferencia3, transferencia4));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/v1/transferencias?inicio=30/08/2015&termino=20/02/2023&nome=Luis")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br");

        String payload = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = objectMapper.getTypeFactory();

        List<TransferenciaResponse> response = objectMapper
                .readValue(payload, typeFactory.constructCollectionType(List.class, TransferenciaResponse.class));

        assertThat(response)
                .hasSize(1)
                .extracting("dataTransferencia", "valor", "tipo", "nomeOperadorTransacao", "contaId")
                .contains(
                        new Tuple(transferencia2.getDataTransferencia(),
                                transferencia2.getValor().setScale(2, RoundingMode.UNNECESSARY),
                                transferencia2.getTipo(),
                                transferencia2.getNomeOperadorTransacao(),
                                transferencia2.getContaId()));
    }
}
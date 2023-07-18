package br.com.banco.services;

import br.com.banco.domain.models.Transferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class TipoPesquisaService {

    private final TransferenciaService service;

    public TipoPesquisaService(TransferenciaService service) {
        this.service = service;
    }

    public Page<Transferencia> getTransferencias(LocalDate inicio,
                                                 LocalDate termino,
                                                 String nome,
                                                 Pageable pageable) {

        if (inicio != null && termino != null && nome != null && !nome.equals("")) {
            return service.getTransferenciasByOperadorEData(pageable, nome, inicio, termino);
        }

        if (nome != null && !nome.equals("")) {
            return service.getTransferenciasByOperador(pageable, nome);
        }

        if (inicio != null && termino != null) {
            return service.getTransferenciasEntrePeriodo(pageable, inicio, termino.plusDays(1));
        }

        return service.getTransferencias(pageable);
    }

    public BigDecimal getSaldo(LocalDate inicio, LocalDate termino) {

        if (inicio != null && termino != null) {
            return service.getSaldo(inicio, termino.plusDays(1));
        }

        return service.getSaldo();
    }
}

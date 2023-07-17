package br.com.banco.services;

import br.com.banco.domain.models.Transferencia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Service
public class TipoPesquisaService {

    private final TransferenciaService service;

    public TipoPesquisaService(TransferenciaService service) {
        this.service = service;
    }

    public List<Transferencia> getTransferencias(
            LocalDate inicio,
            LocalDate termino,
            String nome,
            Pageable pageable) {

        if (inicio != null && termino != null && nome != null) {
            return service.getTransferenciasByOperadorEData(pageable, nome, inicio, termino);
        }

        if (nome != null) {
            return service.getTransferenciasByOperador(pageable, nome);
        }

        if (inicio != null && termino != null) {
            return service.getTransferenciasEntrePeriodo(pageable, inicio, termino.plusDays(1));
        }

        return service.getTransferencias(pageable);
    }
}

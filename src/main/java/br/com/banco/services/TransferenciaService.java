package br.com.banco.services;

import br.com.banco.application.dtos.TransferenciaResponse;
import br.com.banco.domain.repositories.TransferenciaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;

    public TransferenciaService(TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

    public List<TransferenciaResponse> getTransferencias(Pageable pageable) {
        return transferenciaRepository
                .findAll(pageable)
                .stream()
                .map(TransferenciaResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> getTransferenciasEntrePeriodo(Pageable pageable, LocalDate inicio, LocalDate termino) {
        return transferenciaRepository
                .findByDataTransferenciaBetween(pageable, inicio, termino)
                .stream()
                .map(TransferenciaResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> getTransferenciasByOperador(Pageable pageable, String nome) {
        return transferenciaRepository
                .findByNomeOperadorTransacaoContaining(pageable, nome)
                .stream()
                .map(TransferenciaResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> getTransferenciasByOperadorEData(Pageable pageable,
                                                                        String nomeOperador,
                                                                        LocalDate inicio,
                                                                        LocalDate termino) {
        return transferenciaRepository
                .findByDataTransferenciaBetween(pageable, inicio, termino)
                .stream()
                .map(TransferenciaResponse::new)
                .filter(transferencia -> transferencia.getNomeOperadorTransacao() != null)
                .filter(transferencia -> transferencia.getNomeOperadorTransacao().contains(nomeOperador))
                .collect(Collectors.toList());
    }
}

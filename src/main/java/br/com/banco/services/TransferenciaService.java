package br.com.banco.services;

import br.com.banco.application.dtos.TransferenciaResponse;
import br.com.banco.domain.repositories.TransferenciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferenciaService {

    private final TransferenciaRepository repository;

    public TransferenciaService(TransferenciaRepository repository) {
        this.repository = repository;
    }

    public List<TransferenciaResponse> getTransferencias() {
        return repository
                .findAll()
                .stream()
                .map(TransferenciaResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> getTransferenciasEntrePeriodo(LocalDate inicio, LocalDate termino) {
        return repository
                .findByDataTransferenciaBetween(inicio, termino)
                .stream()
                .map(TransferenciaResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransferenciaResponse> getTransferenciaByOperador(String nome) {
        return repository
                .findByNomeOperadorTransferencia(nome)
                .stream()
                .map(TransferenciaResponse::new)
                .collect(Collectors.toList());
    }
}

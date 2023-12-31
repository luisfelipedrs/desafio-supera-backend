package br.com.banco.services;

import br.com.banco.domain.models.Transferencia;
import br.com.banco.domain.repositories.TransferenciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;

    public TransferenciaService(TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

    public Page<Transferencia> getTransferencias(Pageable pageable) {
        return transferenciaRepository
                .findAll(pageable);
    }

    public Page<Transferencia> getTransferenciasEntrePeriodo(Pageable pageable, LocalDate inicio, LocalDate termino) {
        return transferenciaRepository
                .findByDataTransferenciaBetween(pageable, inicio, termino);
    }

    public Page<Transferencia> getTransferenciasByOperador(Pageable pageable, String nome) {
        return transferenciaRepository
                .findByNomeOperadorTransacaoContaining(pageable, nome);
    }

    public Page<Transferencia> getTransferenciasByOperadorEData(Pageable pageable,
                                                                String nomeOperador,
                                                                LocalDate inicio,
                                                                LocalDate termino) {
        List<Transferencia> transferencias = transferenciaRepository
                .findByDataTransferenciaBetween(pageable, inicio, termino)
                .stream()
                .filter(transferencia -> transferencia.getNomeOperadorTransacao() != null)
                .filter(transferencia -> transferencia.getNomeOperadorTransacao().contains(nomeOperador))
                .collect(Collectors.toList());

        return new PageImpl<>(transferencias);
    }

    public BigDecimal getSaldo() {
        return transferenciaRepository
                .findAll()
                .stream()
                .map(Transferencia::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSaldo(LocalDate inicio, LocalDate termino) {
        return transferenciaRepository
                .findByDataTransferenciaBetween(inicio, termino)
                .stream()
                .map(Transferencia::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

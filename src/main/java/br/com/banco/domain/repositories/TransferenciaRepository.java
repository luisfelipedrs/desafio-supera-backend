package br.com.banco.domain.repositories;

import br.com.banco.domain.models.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByDataTransferenciaBetween(LocalDate inicio, LocalDate termino);
    List<Transferencia> findByNomeOperadorTransferencia(String nome);
}

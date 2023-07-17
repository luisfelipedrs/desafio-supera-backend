package br.com.banco.domain.repositories;

import br.com.banco.domain.models.Transferencia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByDataTransferenciaBetween(Pageable pageable, LocalDate inicio, LocalDate termino);
    List<Transferencia> findByDataTransferenciaBetween(LocalDate inicio, LocalDate termino);
    List<Transferencia> findByNomeOperadorTransacaoContaining(Pageable pageable, String nome);
}

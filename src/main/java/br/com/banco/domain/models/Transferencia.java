package br.com.banco.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataTransferencia;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column
    private String nomeOperadorTransacao;

    @Column(nullable = false)
    private Long contaId;

    public Transferencia(LocalDate dataTransferencia, BigDecimal valor, TipoTransacao tipo, String nomeOperadorTransacao, Long contaId) {
        this.dataTransferencia = dataTransferencia;
        this.valor = valor;
        this.tipo = tipo;
        this.nomeOperadorTransacao = nomeOperadorTransacao;
        this.contaId = contaId;
    }
}

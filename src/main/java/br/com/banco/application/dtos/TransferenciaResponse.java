package br.com.banco.application.dtos;

import br.com.banco.domain.models.TipoTransacao;
import br.com.banco.domain.models.Transferencia;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class TransferenciaResponse {

    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataTransferencia;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private String nomeOperadorTransacao;
    private Long contaId;

    public TransferenciaResponse(Transferencia transferencia) {
        this.id = transferencia.getId();
        this.dataTransferencia = transferencia.getDataTransferencia();
        this.valor = transferencia.getValor();
        this.tipo = transferencia.getTipo();
        this.nomeOperadorTransacao = transferencia.getNomeOperadorTransacao();
        this.contaId = transferencia.getContaId();
    }
}

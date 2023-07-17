package br.com.banco.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConta;

    @Column(nullable = false)
    private String nomeResponsavel;

    public Conta(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }
}

package br.com.banco.application;

import br.com.banco.application.dtos.TransferenciaResponse;
import br.com.banco.services.TipoPesquisaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequestMapping("/v1/transferencias")
public class TransferenciaController {

    private final TipoPesquisaService service;

    public TransferenciaController(TipoPesquisaService service) {
        this.service = service;
    }

    @Transactional
    @GetMapping
    public ResponseEntity<Page<TransferenciaResponse>> getTransferencias(@RequestParam(required = false) LocalDate inicio,
                                                                         @RequestParam(required = false) LocalDate termino,
                                                                         @RequestParam(required = false, defaultValue = "") String nome,
                                                                         @PageableDefault(size = 4, page =0, sort = "id", direction = ASC)
                                                                             Pageable pageable) {

        Page<TransferenciaResponse> transferencias = service.getTransferencias(inicio, termino, nome, pageable)
                .map(TransferenciaResponse::new);

        return ResponseEntity.ok(transferencias);
    }

    @Transactional
    @GetMapping("/saldo")
    public ResponseEntity<?> getSaldo(@RequestParam(required = false) LocalDate inicio,
                                               @RequestParam(required = false) LocalDate termino) {

        return ResponseEntity.ok(service.getSaldo(inicio, termino));
    }
}

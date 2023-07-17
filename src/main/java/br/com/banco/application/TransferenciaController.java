package br.com.banco.application;

import br.com.banco.application.dtos.TransferenciaResponse;
import br.com.banco.services.TipoPesquisaService;
import br.com.banco.services.TransferenciaService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/transferencias")
public class TransferenciaController {

    private final TipoPesquisaService service;

    public TransferenciaController(TipoPesquisaService service) {
        this.service = service;
    }

    @Transactional
    @GetMapping
    public ResponseEntity<List<TransferenciaResponse>> getTransferencias(@RequestParam(required = false) LocalDate inicio,
                                                                         @RequestParam(required = false) LocalDate termino,
                                                                         @RequestParam(required = false) String nome,
                                                                         @PageableDefault(size = 4,
                                                                                 page = 0,
                                                                                 sort = "id",
                                                                                 direction = Sort.Direction.ASC)
                                                                             Pageable pageable) {

        return ResponseEntity.ok(service.getTransferencias(inicio, termino, nome, pageable)
                        .stream()
                        .map(TransferenciaResponse::new)
                        .collect(Collectors.toList()));
    }
}

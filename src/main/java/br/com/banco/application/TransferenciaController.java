package br.com.banco.application;

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

@RestController
@RequestMapping("/v1/transferencias")
public class TransferenciaController {

    private final TransferenciaService service;

    public TransferenciaController(TransferenciaService service) {
        this.service = service;
    }

    @Transactional
    @GetMapping
    public ResponseEntity<?> getTransferencias(@RequestParam(required = false) LocalDate inicio,
                                               @RequestParam(required = false) LocalDate termino,
                                               @RequestParam(required = false) String nome,
                                               @PageableDefault(size = 4, page = 0, sort = "id", direction = Sort.Direction.ASC)
                                                   Pageable pageable) {

        if (inicio != null && termino != null && nome != null) {
            return ResponseEntity
                    .ok(service.getTransferenciasByOperadorEData(pageable, nome, inicio, termino));
        }

        if (nome != null) {
            return ResponseEntity
                    .ok(service.getTransferenciasByOperador(pageable, nome));
        }

        if (inicio != null && termino != null) {
            return ResponseEntity
                    .ok(service.getTransferenciasEntrePeriodo(pageable, inicio, termino.plusDays(1)));
        }

        return ResponseEntity
                .ok(service.getTransferencias(pageable));
    }
}

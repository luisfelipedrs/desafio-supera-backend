package br.com.banco.application;

import br.com.banco.services.TransferenciaService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    private final TransferenciaService service;

    public TransferenciaController(TransferenciaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getTransferencias(@RequestParam @JsonFormat(pattern = "dd/MM/yyyy")Optional<LocalDate> inicio,
                                               @RequestParam @JsonFormat(pattern = "dd/MM/yyyy")Optional<LocalDate> termino) {

        if (inicio.isPresent() && termino.isPresent()) {
            return ResponseEntity.ok(service.getTransferenciasEntrePeriodo(inicio.get(), termino.get()));
        }

        return ResponseEntity.ok(service.getTransferencias());
    }
}

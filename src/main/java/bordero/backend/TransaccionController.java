package bordero.backend;

import kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransaccionController {
    @Autowired
    Producer<TransaccionModel> producer;
    private final  TransaccionService transaccionService;

    TransaccionController(TransaccionService transaccionService){
        this.transaccionService = transaccionService;
    }

    @GetMapping("/transacciones")
    List<TransaccionModel> obtenerTransacciones() {
        return transaccionService.obtenerTransacciones();
    }

    @GetMapping("/extracciones")
    List<TransaccionModel> obtenerExtracciones() {
        return transaccionService.obtenerExtracciones();
    }

    @PostMapping("/deposito")
    TransaccionModel nuevoDeposito(@RequestBody TransaccionModel nuevoDeposito) {
        TransaccionModel transaccionResponse = transaccionService.nuevoDeposito(nuevoDeposito);
        producer.logCreate("operaciones", transaccionResponse);
        return transaccionResponse;
    }

    @PostMapping("/extraccion")
    TransaccionModel nuevaExtraccion(@RequestBody TransaccionModel nuevaExtraccion) {
        TransaccionModel transaccionResponse = transaccionService.nuevaExtraccion(nuevaExtraccion);
        producer.logCreate("operaciones", transaccionResponse);
        return transaccionResponse;
    }

    @PostMapping("/interes")
    TransaccionModel nuevoInteres(@RequestBody InteresDTO interesDTO) {
        TransaccionModel transaccionResponse = transaccionService.nuevoInteres(interesDTO.interes);
        producer.logCreate("operaciones", transaccionResponse);
        return transaccionResponse;
    }

}


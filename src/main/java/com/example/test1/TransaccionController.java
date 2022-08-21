package com.example.test1;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransaccionController {

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
        return transaccionService.nuevoDeposito(nuevoDeposito);
    }

    @PostMapping("/extraccion")
    TransaccionModel nuevaExtraccion(@RequestBody TransaccionModel nuevaExtraccion) {
        return transaccionService.nuevaExtraccion(nuevaExtraccion);
    }

    @PostMapping("/interes")
    TransaccionModel nuevaExtraccion(@RequestBody InteresDTO extraccionDTO) {
        return transaccionService.nuevoInteres(extraccionDTO.interes);
    }





}


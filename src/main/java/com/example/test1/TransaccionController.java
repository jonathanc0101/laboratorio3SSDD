package com.example.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@RestController
public class TransaccionController {

    private final  TransaccionService transaccionService;

    TransaccionController(TransaccionService transaccionService){
        this.transaccionService = transaccionService;
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/saldo")
    public Saldo returnSaldo() {
        return new Saldo(1000);
    }

    @PostMapping("/deposito")
    TransaccionModel nuevoDeposito(@RequestBody TransaccionModel nuevoDeposito) {
        return transaccionService.nuevoDeposito(nuevoDeposito);
    }

    @PostMapping("/extraccion")
    TransaccionModel nuevaExtraccion(@RequestBody TransaccionModel nuevaExtraccion) {
        return transaccionService.nuevaExtraccion(nuevaExtraccion);
    }

  //  @PostMapping("/interes")





}


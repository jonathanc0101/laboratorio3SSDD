package com.example.test1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaldoController {

    private final  TransaccionService transaccionService;


    SaldoController(TransaccionService transaccionService){
        this.transaccionService = transaccionService;
    }


    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/saldo")
    public Saldo returnSaldo() {
        return transaccionService.getSaldoActual();
    }

}

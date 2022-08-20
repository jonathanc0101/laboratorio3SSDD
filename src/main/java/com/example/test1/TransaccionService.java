package com.example.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransaccionService {

    private final  TransaccionRepository transaccionRepository;

    TransaccionService(TransaccionRepository transaccionRepository){
        this.transaccionRepository = transaccionRepository;
    }

    public TransaccionModel nuevoDeposito(TransaccionModel transaccion){
        transaccion.ttype = TransaccionModel.TRANSACTION_TYPE.DEPOSITO;
        return transaccionRepository.save(transaccion);
    }

    public TransaccionModel nuevaExtraccion(TransaccionModel transaccion){
        transaccion.monto *= -1;
        transaccion.ttype = TransaccionModel.TRANSACTION_TYPE.EXTRACCION;
        return transaccionRepository.save(transaccion);
    }

}

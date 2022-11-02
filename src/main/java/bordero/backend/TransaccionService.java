package bordero.backend;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionService {

    private final  TransaccionRepository transaccionRepository;

    TransaccionService(TransaccionRepository transaccionRepository){
        this.transaccionRepository = transaccionRepository;
    }


    public TransaccionModel nuevoDeposito(TransaccionModel transaccion){
        transaccion.setType(TransaccionModel.TRANSACTION_TYPE.DEPOSITO) ;
        return transaccionRepository.save(transaccion);
    }

    public TransaccionModel nuevaExtraccion(TransaccionModel transaccion){
        transaccion.monto *= -1;
        transaccion.setType(TransaccionModel.TRANSACTION_TYPE.EXTRACCION);
        return transaccionRepository.save(transaccion);
    }

    public TransaccionModel nuevoInteres(double porcentaje){
        TransaccionModel transaccion = new TransaccionModel();
        transaccion.setType(TransaccionModel.TRANSACTION_TYPE.INTERES) ;
        transaccion.monto = this.getSaldoActual().saldo * porcentaje / 100;
        return transaccionRepository.save(transaccion);
    }

    public TransaccionModel insert(TransaccionModel transaccion){
        return transaccionRepository.save(transaccion);
    }

    public Saldo getSaldoActual() {
        List<TransaccionModel> transacciones = this.obtenerTransacciones();

        Saldo saldo = new Saldo();

        for(TransaccionModel transaccion : transacciones) {
            saldo.saldo += transaccion.monto;
        }
        return saldo;
    }

    public List<TransaccionModel> obtenerTransacciones() {
        return transaccionRepository.findAll();
    }

    public List<TransaccionModel> obtenerExtracciones() {
        return transaccionRepository.findAllByTtype(TransaccionModel.TRANSACTION_TYPE.EXTRACCION);
    }


}

package bordero.backend;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends CrudRepository<TransaccionModel, Long> {
    List<TransaccionModel> findTransaccionesByTtype(TransaccionModel.TRANSACTION_TYPE type);
    List<TransaccionModel> findAll();
    List<TransaccionModel> findAllByTtype(TransaccionModel.TRANSACTION_TYPE type);

}
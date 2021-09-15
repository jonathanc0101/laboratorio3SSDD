package bordero.backend.service;

import bordero.backend.model.Bordero;

public interface BorderoService extends CRUDService<Bordero> {

    int ERROR_BORDERO_UNSPECIFIED = 1001;
    int ERROR_BORDERO_CUSTOMER_MISSING = 1002;

}

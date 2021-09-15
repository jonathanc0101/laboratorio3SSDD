package bordero.backend.service;

import bordero.backend.model.Customer;

public interface CustomerService extends CRUDService<Customer> {

    int ERROR_CUSTOMER_UNSPECIFIED = 2001;
    int ERROR_CUSTOMER_NAME_MISSING = 2002;

}

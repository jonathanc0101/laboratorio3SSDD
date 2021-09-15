package bordero.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bordero.backend.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> { }
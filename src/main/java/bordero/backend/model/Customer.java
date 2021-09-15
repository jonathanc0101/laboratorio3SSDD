package bordero.backend.model;

import javax.persistence.*;

@Entity
public class Customer implements Identificable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "GenCustomer"
    )
    @SequenceGenerator(
            name = "GenCustomer",
            sequenceName = "customer_sq",
            allocationSize = 5
    )
    private long id;

    private String name;

    public Customer() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer(String name) { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

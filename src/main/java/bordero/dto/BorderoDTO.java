package bordero.dto;

import java.time.LocalDate;

public class BorderoDTO extends DTO {

    public final LocalDate date;
    public final CustomerDTO customer;

    public BorderoDTO(long id, LocalDate date, CustomerDTO customer) {
        super(id);
        this.date = date;
        this.customer = customer;
    }

    public BorderoDTO(LocalDate date, CustomerDTO customer) {
        this(0, date, customer);
    }

    public BorderoDTO() {
        this(0, null, null);
    }

}

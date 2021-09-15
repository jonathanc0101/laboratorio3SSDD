package bordero.dto;

public class CustomerDTO extends DTO {

    public final String name;

    public CustomerDTO(String name) {
        this(0, name);
    }

    public CustomerDTO(long id, String name) {
        super(id);
        this.name = name;
    }

    public CustomerDTO() {
        this(0,null);
    }


}

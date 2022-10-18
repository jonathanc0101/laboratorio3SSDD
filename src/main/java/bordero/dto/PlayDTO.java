package bordero.dto;

import lombok.ToString;

@ToString
public class PlayDTO extends DTO {

    public final String code;
    public final String name;
    public final String type;

    public PlayDTO(long id, String code, String name, String type) {
        super(id);
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public PlayDTO(String code, String name, String type) {
        this(0, code, name, type);
    }

    public PlayDTO() {
        this(0, null, null, null);
    }
}

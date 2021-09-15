package bordero.backend.model;

import javax.persistence.*;

@Entity
public class Play implements Identificable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "GenPlay"
    )
    @SequenceGenerator(
            name = "GenPlay",
            sequenceName = "play_sq",
            allocationSize = 5
    )
    private long id;
    private String code;
    private String name;

    @Enumerated(EnumType.STRING)
    private PlayType type;

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public PlayType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setType(PlayType type) {
        this.type = type;
    }

    public Play(String code, String name, PlayType type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public Play() {  }
}

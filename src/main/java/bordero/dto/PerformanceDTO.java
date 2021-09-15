package bordero.dto;

public class PerformanceDTO extends DTO {

    public final int audience;
    public final PlayDTO play;
    public final BorderoDTO bordero;

    public PerformanceDTO(long id, int audience, PlayDTO play, BorderoDTO bordero) {
        super(id);
        this.audience = audience;
        this.play = play;
        this.bordero = bordero;
    }

    public PerformanceDTO(int audience, PlayDTO play, BorderoDTO bordero) {
        this(0, audience, play, bordero);
    }

    public PerformanceDTO() {
        this(0, 0, null, null);
    }

}

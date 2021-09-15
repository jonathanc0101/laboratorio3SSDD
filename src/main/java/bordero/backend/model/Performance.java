package bordero.backend.model;

import javax.persistence.*;

@Entity
public class Performance implements Identificable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "GenPerformance"
    )
    @SequenceGenerator(
            name = "GenPerformance",
            sequenceName = "performance_sq",
            allocationSize = 5
    )
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "bordero_id", nullable=false)
    private Bordero bordero;

    public Bordero getBordero() {
        return bordero;
    }

    public void setBordero(Bordero bordero) {
        this.bordero = bordero;
    }

    private int audience;

    @ManyToOne
    @JoinColumn(name = "play_id")
    private Play play;

    public Performance() {
    }

    public int getAudience() {
        return audience;
    }

    public Play getPlay() {
        return play;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public Performance(Play play, int audience) {
        this.audience = audience;
        this.play = play;
    }

}

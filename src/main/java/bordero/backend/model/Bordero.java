package bordero.backend.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Bordero implements Identificable {

  @Id
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "GenBordero"
  )
  @SequenceGenerator(
          name = "GenBordero",
          sequenceName = "bordero_sq",
          allocationSize = 5
  )
  private long id;

  private LocalDate date;

  @ManyToOne
  private Customer customer;

  @OneToMany(mappedBy = "bordero")
  private List<Performance> performances;

  public Bordero() {
    this.performances = new ArrayList<>();
  }

  public long getId() {
    return id;
  }

  public LocalDate getDate() {
    return date;
  }

  public Customer getCustomer() {
    return customer;
  }

  public List<Performance> getPerformances() {
    return performances;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public void setPerformances(List<Performance> performances) {
    this.performances = performances;
  }

  public void addPerformance(Performance aPerformance) {
    performances.add(aPerformance);
  }

  public void removePerformance(Performance aPerformance) {
    performances.remove(aPerformance);
  }

}

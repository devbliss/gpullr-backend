package com.devbliss.gpullr.domain;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Represents a pullrequest persisted in our application.
 *
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 */
@Entity
public class Pullrequest {

  public enum State {
    OPEN, CLOSED
  }

  @Id
  @NotNull
  @Min(1)
  public Integer id;

  @ManyToOne(optional = false)
  public Repo repo;

  @ManyToOne(optional = false)
  public User owner;

  public String title;

  public String url;

  public ZonedDateTime createdAt;

  @NotNull
  @Enumerated(EnumType.STRING)
  public State state;

  /**
   * Number of the PR (unique only within the same repository!).
   */
  public Integer number;

  public Integer additions;

  public Integer deletions;

  public Integer changedFiles;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Pullrequest other = (Pullrequest) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}

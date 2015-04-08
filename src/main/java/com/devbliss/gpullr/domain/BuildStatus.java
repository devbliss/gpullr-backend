package com.devbliss.gpullr.domain;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Represents the current CI build status of a pull request or branch respectively.
 * 
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 *
 */
@Embeddable
public class BuildStatus {

  public enum State {
    SUCCESS, PENDING, FAILURE;

    public static State parse(String stringRepresentation) {
      return valueOf(stringRepresentation.toUpperCase());
    }
  }

  public BuildStatus() {}

  public BuildStatus(State state, ZonedDateTime timestamp, String buildUri) {
    this.state = state;
    this.timestamp = timestamp;
    this.buildUri = buildUri;
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "build_state")
  public State state;

  @Column(name = "build_timestamp")
  public ZonedDateTime timestamp;

  /**
   * URI pointing to the CI build (e.g. Jenkins)
   */
  @Column(name = "build_uri")
  public String buildUri;
}

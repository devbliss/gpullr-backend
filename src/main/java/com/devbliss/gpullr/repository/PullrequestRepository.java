package com.devbliss.gpullr.repository;

import java.util.List;

import com.devbliss.gpullr.domain.Pullrequest;
import org.springframework.data.repository.CrudRepository;

/**
 * Handles persistence of {@link Pullrequest} entities.
 * 
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 *
 */
public interface PullrequestRepository extends CrudRepository<Pullrequest, Integer>{

  List<Pullrequest> findAll();
}

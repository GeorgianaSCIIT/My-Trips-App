package ro.siit.travel.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.siit.travel.model.Trip;
/**
 * Trip Repository
 *
 * @author Georgiana Simpetreanu
 * @version 1.0
 * Date 10/21/2019.
 */
@Repository
public interface TripRepository extends CrudRepository<Trip, Integer> {

}

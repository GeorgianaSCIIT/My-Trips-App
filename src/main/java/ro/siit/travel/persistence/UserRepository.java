package ro.siit.travel.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.siit.travel.model.User;
/**
 * User Repository
 *
 * @author Georgiana Simpetreanu
 * @version 1.0
 * Date 10/21/2019.
 */



@Repository
public interface   UserRepository extends CrudRepository<User, Integer> {

    User findByUserName(String userName);

    User findById(int id);

    User findByEmail(String email);


}

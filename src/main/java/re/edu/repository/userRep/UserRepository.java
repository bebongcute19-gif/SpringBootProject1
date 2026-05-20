package re.edu.repository.userRep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import re.edu.model.enums.Role;
import re.edu.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);
    Optional<User> getUserById(@Param("id") Integer id);
    List<User> findAllByRole(Role role);
}
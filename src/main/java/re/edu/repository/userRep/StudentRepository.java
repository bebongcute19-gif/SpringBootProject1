package re.edu.repository.userRep;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.model.entity.Student;
;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
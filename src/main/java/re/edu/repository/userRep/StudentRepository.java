package re.edu.repository.userRep;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.model.entity.Student;
;import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findDistinctByAssignments_Mentor_Id(Integer mentorId);
    boolean existsByStudentCode(String studentCode);
    boolean existsById(Integer id);
}
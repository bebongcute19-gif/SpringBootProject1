package re.edu.repository.internshipRep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.model.entity.InternshipPhase;

@Repository
public interface InternshipPhaseRepository
        extends JpaRepository<InternshipPhase, Integer> {
}
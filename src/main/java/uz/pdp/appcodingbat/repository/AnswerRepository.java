package uz.pdp.appcodingbat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcodingbat.entity.Answer;
import uz.pdp.appcodingbat.entity.Example;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByTaskId(Integer task_id);

    List<Answer> findAllByUserId(Integer user_id);
}

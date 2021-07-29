package uz.pdp.appcodingbat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcodingbat.entity.Example;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.entity.Task;

import java.util.List;
import java.util.Optional;

public interface ExampleRepository extends JpaRepository<Example, Integer> {
    boolean existsByTextAndTaskIdAndStatusTrue(String text, Integer task_id);

    boolean existsByTextAndTaskIdAndStatusTrueAndIdNot(String text, Integer task_id, Integer id);

    List<Example> findAllByTaskIdAndStatusTrue(Integer task_id);

    Optional<Example> findByIdAndStatusTrue(Integer integer);
}

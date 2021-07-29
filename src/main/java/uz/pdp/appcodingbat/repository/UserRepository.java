package uz.pdp.appcodingbat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmailAndStatusTrue(String email);

    boolean existsByEmailAndIdNotAndStatusTrue(String email, Integer id);

    List<User> findAllByStatusTrue();

    Optional<User> findByIdAndStatusTrue(Integer integer);
}

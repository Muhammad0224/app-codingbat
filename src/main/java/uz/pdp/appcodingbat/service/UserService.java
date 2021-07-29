package uz.pdp.appcodingbat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcodingbat.entity.Answer;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.entity.User;
import uz.pdp.appcodingbat.helpers.Utils;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.model.UserDto;
import uz.pdp.appcodingbat.repository.LanguageRepository;
import uz.pdp.appcodingbat.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerService answerService;

    public ResponseEntity<List<User>> get() {
        return ResponseEntity.ok(userRepository.findAllByStatusTrue());
    }

    public ResponseEntity<User> get(Integer id) {
        Optional<User> optionalUser = userRepository.findByIdAndStatusTrue(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Result> add(UserDto dto) {
        if (userRepository.existsByEmailAndStatusTrue(dto.getEmail()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        userRepository.save(User.builder().email(dto.getEmail()).password(dto.getPassword()).status(true).build());
        return ResponseEntity.ok(new Result("User added", true));
    }

    public ResponseEntity<Result> edit(Integer id, UserDto dto) {
        if (userRepository.existsByEmailAndIdNotAndStatusTrue(dto.getEmail(), id))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<User> optionalUser = userRepository.findByIdAndStatusTrue(id);
        if (!optionalUser.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = optionalUser.get();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(new Result("User edited", true));
    }

    public ResponseEntity<Result> delete(Integer id) {
        Optional<User> optionalUser = userRepository.findByIdAndStatusTrue(id);
        if (!optionalUser.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = optionalUser.get();
        List<Answer> answers = answerService.getByUser(user.getId()).getBody();
        if (!Utils.isEmpty(answers)){
            for (Answer answer : answers) {
                answerService.delete(answer.getId());
            }
        }
        user.setStatus(false);
        userRepository.save(user);
        return ResponseEntity.ok(new Result("User deleted",true));
    }
}

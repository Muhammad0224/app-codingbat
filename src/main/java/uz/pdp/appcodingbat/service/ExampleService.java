package uz.pdp.appcodingbat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcodingbat.entity.Example;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.entity.Task;
import uz.pdp.appcodingbat.model.ExampleDto;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.repository.ExampleRepository;
import uz.pdp.appcodingbat.repository.LanguageRepository;
import uz.pdp.appcodingbat.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExampleService {

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private TaskRepository taskRepository;

    public ResponseEntity<List<Example>> getByTask(Integer taskId) {
        return ResponseEntity.ok(exampleRepository.findAllByTaskIdAndStatusTrue(taskId));
    }

    public ResponseEntity<Example> get(Integer id) {
        Optional<Example> optionalExample = exampleRepository.findByIdAndStatusTrue(id);
        return optionalExample.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Result> add(ExampleDto dto) {
        if (exampleRepository.existsByTextAndTaskIdAndStatusTrue(dto.getText(), dto.getTaskId()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<Task> optionalTask = taskRepository.findByIdAndStatusTrue(dto.getTaskId());
        if (!optionalTask.isPresent())
            return new ResponseEntity<>(new Result("Task not found", false), HttpStatus.NOT_FOUND);

        exampleRepository.save(Example.builder().task(optionalTask.get()).status(true).text(dto.getText()).build());
        return ResponseEntity.ok(new Result("Example added", true));
    }

    public ResponseEntity<Result> edit(Integer id, ExampleDto dto) {
        if (exampleRepository.existsByTextAndTaskIdAndStatusTrueAndIdNot(dto.getText(), dto.getTaskId(), id))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<Example> optionalExample = exampleRepository.findByIdAndStatusTrue(id);
        if (!optionalExample.isPresent())
            return new ResponseEntity<>(new Result("Example not found", false), HttpStatus.NOT_FOUND);

        Optional<Task> optionalTask = taskRepository.findByIdAndStatusTrue(dto.getTaskId());
        if (!optionalTask.isPresent())
            return new ResponseEntity<>(new Result("Task not found", false), HttpStatus.NOT_FOUND);

        Example example = optionalExample.get();
        example.setTask(optionalTask.get());
        example.setText(dto.getText());
        exampleRepository.save(example);
        return ResponseEntity.ok(new Result("Example edited", true));
    }

    public ResponseEntity<Result> delete(Integer id) {
        Optional<Example> optionalExample = exampleRepository.findByIdAndStatusTrue(id);
        if (!optionalExample.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Example example = optionalExample.get();
        example.setStatus(false);
        exampleRepository.save(example);
        return ResponseEntity.ok(new Result("Example deleted", true));
    }
}

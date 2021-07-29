package uz.pdp.appcodingbat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcodingbat.entity.*;
import uz.pdp.appcodingbat.helpers.Utils;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.model.TaskDto;
import uz.pdp.appcodingbat.repository.CategoryRepository;
import uz.pdp.appcodingbat.repository.LanguageRepository;
import uz.pdp.appcodingbat.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ExampleService exampleService;

    @Autowired
    private AnswerService answerService;

    public ResponseEntity<List<Task>> get() {
        return ResponseEntity.ok(taskRepository.findAllByStatusTrue());
    }

    public ResponseEntity<List<Task>> getByCategory(Integer languageId, Integer categoryId) {
        return ResponseEntity.ok(taskRepository.findAllByCategoryIdAndLanguageIdAndStatusTrue(categoryId, languageId));
    }

    public ResponseEntity<Task> get(Integer id) {
        Optional<Task> optionalTask = taskRepository.findByIdAndStatusTrue(id);
        return optionalTask.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Result> add(TaskDto dto) {
        if (taskRepository.existsByNameAndCategoryIdAndStatusTrue(dto.getName(), dto.getCategoryId()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatusTrue(dto.getCategoryId());
        if (!optionalCategory.isPresent())
            return new ResponseEntity<>(new Result("Category not found",false), HttpStatus.NOT_FOUND);

        Optional<Language> optionalLanguage = languageRepository.findByIdAndStatusTrue(dto.getLanguageId());
        if (!optionalLanguage.isPresent())
            return new ResponseEntity<>(new Result("Language not found",false),HttpStatus.NOT_FOUND);

        taskRepository.save(Task.builder()
                .name(dto.getName())
                .category(optionalCategory.get())
                .hasStar(dto.isHasStar())
                .hint(dto.getHint())
                .method(dto.getMethod())
                .solution(dto.getSolution())
                .text(dto.getText())
                .status(true)
                .language(optionalLanguage.get()).build());
        return ResponseEntity.ok(new Result("Task added", true));
    }

    public ResponseEntity<Result> edit(Integer id, TaskDto dto) {
        if (taskRepository.existsByNameAndCategoryIdAndStatusTrueAndIdNot(dto.getName(), dto.getCategoryId(), id))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<Task> optionalTask = taskRepository.findByIdAndStatusTrue(id);
        if (!optionalTask.isPresent())
            return new ResponseEntity<>(new Result("Task not found", false), HttpStatus.NOT_FOUND);

        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatusTrue(dto.getCategoryId());
        if (!optionalCategory.isPresent())
            return new ResponseEntity<>(new Result("Category not found",false), HttpStatus.NOT_FOUND);

        Optional<Language> optionalLanguage = languageRepository.findByIdAndStatusTrue(dto.getLanguageId());
        if (!optionalLanguage.isPresent())
            return new ResponseEntity<>(new Result("Language not found",false),HttpStatus.NOT_FOUND);

        Task task = optionalTask.get();
        task.setName(dto.getName());
        task.setCategory(optionalCategory.get());
        task.setHint(dto.getHint());
        task.setHasStar(dto.isHasStar());
        task.setLanguage(optionalLanguage.get());
        task.setMethod(dto.getMethod());
        task.setText(dto.getText());
        task.setSolution(dto.getSolution());
        taskRepository.save(task);
        return ResponseEntity.ok(new Result("Task edited", true));
    }

    public ResponseEntity<Result> delete(Integer id) {
        Optional<Task> optionalTask = taskRepository.findByIdAndStatusTrue(id);
        if (!optionalTask.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Task task = optionalTask.get();
        List<Example> examples = exampleService.getByTask(task.getId()).getBody();
        if (!Utils.isEmpty(examples)){
            for (Example example : examples) {
                exampleService.delete(example.getId());
            }
        }
        List<Answer> answers = answerService.getByTask(task.getId()).getBody();
        if (!Utils.isEmpty(answers)){
            for (Answer answer  : answers) {
                answerService.delete(answer.getId());
            }
        }
        task.setStatus(false);
        taskRepository.save(task);
        return ResponseEntity.ok(new Result("Task deleted",true));
    }


}

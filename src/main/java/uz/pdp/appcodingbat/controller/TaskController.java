package uz.pdp.appcodingbat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.entity.Task;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.model.TaskDto;
import uz.pdp.appcodingbat.service.LanguageService;
import uz.pdp.appcodingbat.service.TaskService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> get(){
        return taskService.get();
    }

    @GetMapping("/language/{languageId}/category/{categoryId}")
    public ResponseEntity<List<Task>> getByCategory(@PathVariable Integer languageId, @PathVariable Integer categoryId){
        return taskService.getByCategory(languageId,categoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable Integer id){
        return taskService.get(id);
    }

    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody TaskDto dto){
        return taskService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@Valid @PathVariable Integer id, @RequestBody TaskDto dto){
        return taskService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id){
        return taskService.delete(id);
    }
}

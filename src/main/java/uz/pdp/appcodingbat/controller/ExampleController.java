package uz.pdp.appcodingbat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcodingbat.entity.Example;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.model.ExampleDto;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.service.ExampleService;
import uz.pdp.appcodingbat.service.LanguageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @Autowired
    private ExampleService exampleService;

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Example>> getByTask(@PathVariable Integer taskId){
        return exampleService.getByTask(taskId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Example> get(@PathVariable Integer id){
        return exampleService.get(id);
    }

    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody ExampleDto dto){
        return exampleService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@Valid @PathVariable Integer id, @RequestBody ExampleDto dto){
        return exampleService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id){
        return exampleService.delete(id);
    }
}

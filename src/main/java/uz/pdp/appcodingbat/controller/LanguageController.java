package uz.pdp.appcodingbat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.service.LanguageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/language")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping
    public ResponseEntity<List<Language>> get(){
        return languageService.get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Language> get(@PathVariable Integer id){
        return languageService.get(id);
    }

    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody LanguageDto dto){
        return languageService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@Valid @PathVariable Integer id, @RequestBody LanguageDto dto){
        return languageService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id){
        return languageService.delete(id);
    }
}

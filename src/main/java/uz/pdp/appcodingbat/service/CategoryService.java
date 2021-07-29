package uz.pdp.appcodingbat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcodingbat.entity.Category;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.entity.Task;
import uz.pdp.appcodingbat.helpers.Utils;
import uz.pdp.appcodingbat.model.CategoryDto;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.repository.CategoryRepository;
import uz.pdp.appcodingbat.repository.LanguageRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TaskService taskService;

    public ResponseEntity<List<Category>> get() {
        return ResponseEntity.ok(categoryRepository.findAllByStatusTrue());
    }

    public ResponseEntity<Category> get(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatusTrue(id);
        return optionalCategory.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Category>> getByLanguage(Integer languageId) {
        return ResponseEntity.ok(categoryRepository.findAllByLanguage(languageId));
    }

    public ResponseEntity<Result> add(CategoryDto dto) {
        if (categoryRepository.existsByNameAndStatusTrue(dto.getName()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Set<Language> languages = getLanguages(dto.getLanguageIds());
        if (Utils.isEmpty(languages))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        categoryRepository.save(Category.builder().name(dto.getName()).status(true).language(languages).description(dto.getDescription()).build());
        return ResponseEntity.ok(new Result("Category added", true));
    }

    public ResponseEntity<Result> edit(Integer id, CategoryDto dto) {
        if (categoryRepository.existsByNameAndIdNotAndStatusTrue(dto.getName(), id))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatusTrue(id);
        if (!optionalCategory.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Language> languages = getLanguages(dto.getLanguageIds());
        if (Utils.isEmpty(languages))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Category category = optionalCategory.get();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setLanguage(languages);
        categoryRepository.save(category);
        return ResponseEntity.ok(new Result("Category edited", true));
    }

    public ResponseEntity<Result> delete(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatusTrue(id);
        if (!optionalCategory.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Category category = optionalCategory.get();
        List<Task> tasks = taskService.getByCategory(category.getId()).getBody();
        if (!Utils.isEmpty(tasks)){
            for (Task task : tasks) {
                taskService.delete(task.getId());
            }
        }
        category.setStatus(false);
        categoryRepository.save(category);
        return ResponseEntity.ok(new Result("Category deleted", true));
    }

    /**
     * Custom method
     * @param languageIds
     * @return
     */
    public Set<Language> getLanguages(Set<Integer> languageIds){
        Set<Language> languages = new HashSet<>();
        for (Integer languageId : languageIds) {
            Optional<Language> optionalLanguage = languageRepository.findByIdAndStatusTrue(languageId);
            if (!optionalLanguage.isPresent())
                return null;
            languages.add(optionalLanguage.get());
        }
        return languages;
    }

}

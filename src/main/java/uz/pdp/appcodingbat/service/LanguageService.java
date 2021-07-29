package uz.pdp.appcodingbat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcodingbat.entity.Category;
import uz.pdp.appcodingbat.entity.Language;
import uz.pdp.appcodingbat.helpers.Utils;
import uz.pdp.appcodingbat.model.LanguageDto;
import uz.pdp.appcodingbat.model.Result;
import uz.pdp.appcodingbat.repository.LanguageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CategoryService categoryService;

    public ResponseEntity<List<Language>> get() {
        return ResponseEntity.ok(languageRepository.findAllByStatusTrue());
    }

    public ResponseEntity<Language> get(Integer id) {
        Optional<Language> optionalLanguage = languageRepository.findByIdAndStatusTrue(id);
        return optionalLanguage.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Result> add(LanguageDto dto) {
        if (languageRepository.existsByNameAndStatusTrue(dto.getName()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        languageRepository.save(Language.builder().name(dto.getName()).status(true).build());
        return ResponseEntity.ok(new Result("Language added", true));
    }

    public ResponseEntity<Result> edit(Integer id, LanguageDto dto) {
        if (languageRepository.existsByNameAndIdNotAndStatusTrue(dto.getName(), id))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Optional<Language> optionalLanguage = languageRepository.findByIdAndStatusTrue(id);
        if (!optionalLanguage.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Language language = optionalLanguage.get();
        language.setName(dto.getName());
        languageRepository.save(language);
        return ResponseEntity.ok(new Result("Language edited", true));
    }

    public ResponseEntity<Result> delete(Integer id) {
        Optional<Language> optionalLanguage = languageRepository.findByIdAndStatusTrue(id);
        if (!optionalLanguage.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Language language = optionalLanguage.get();
        List<Category> categories = categoryService.getByLanguage(language.getId()).getBody();
        if (!Utils.isEmpty(categories)){
            for (Category category : categories) {
                categoryService.delete(category.getId());
            }
        }
        language.setStatus(false);
        languageRepository.save(language);
        return ResponseEntity.ok(new Result("Language deleted",true));
    }
}

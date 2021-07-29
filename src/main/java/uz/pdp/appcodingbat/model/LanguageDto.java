package uz.pdp.appcodingbat.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageDto {
    @NotNull
    private String name;
}

package uz.pdp.appcodingbat.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    @NotNull
    private String name;

    private String text;

    @NotNull
    private String solution;

    private String hint;

    @NotNull
    private String method;

    private boolean hasStar;

    @NotNull
    private Integer languageId;

    @NotNull
    private Integer categoryId;
}

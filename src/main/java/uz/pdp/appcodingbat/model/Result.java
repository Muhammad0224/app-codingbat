package uz.pdp.appcodingbat.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    private String message;
    private boolean state;
}

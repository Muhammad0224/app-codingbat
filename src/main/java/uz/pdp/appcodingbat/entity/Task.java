package uz.pdp.appcodingbat.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String text;

    @Column
    private String solution;

    @Column
    private String hint;

    @Column
    private String method;

    @Column
    private Boolean hasStar;

    @ManyToMany
    private Language language;

    @ManyToOne(optional = false)
    private Category category;
}

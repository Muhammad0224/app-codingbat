package uz.pdp.appcodingbat.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category_id"}))
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String text;

    @Column(nullable = false)
    private String solution;

    @Column
    private String hint;

    @Column(nullable = false)
    private String method;

    @Column
    private boolean hasStar;

    @ManyToOne(optional = false)
    private Language language;

    @ManyToOne(optional = false)
    private Category category;

    private boolean status;
}

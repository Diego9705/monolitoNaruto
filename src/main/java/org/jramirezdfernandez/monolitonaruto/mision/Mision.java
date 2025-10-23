package org.jramirezdfernandez.monolitonaruto.mision;


import jakarta.persistence.*;
import lombok.*;
import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name="t_mision")
public class Mision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String rank;

    private Integer recompensa;

    private String requisitorango;

    @ManyToOne
    @JoinColumn(name = "ninja_id")
    private Ninja ninja;


}

package org.jramirezdfernandez.monolitonaruto.jutsus;


import jakarta.persistence.*;
import lombok.*;
import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name="t_jutsus")
public class Jutsu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


}
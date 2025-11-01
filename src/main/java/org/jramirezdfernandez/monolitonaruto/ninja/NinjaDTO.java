package org.jramirezdfernandez.monolitonaruto.ninja;

import lombok.Data;
import org.jramirezdfernandez.monolitonaruto.aldea.Aldea;
import org.jramirezdfernandez.monolitonaruto.jutsu.Jutsu;

import java.util.List;


@Data
public class NinjaDTO {
    private Long id;
    private String name;
    private String rank;
    private Integer atk;
    private Integer def;
    private Integer chakra;
    private Aldea aldea;
    private List<String> jutsus;
}
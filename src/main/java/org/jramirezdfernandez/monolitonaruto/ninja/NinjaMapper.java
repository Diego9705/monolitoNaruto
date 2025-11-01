package org.jramirezdfernandez.monolitonaruto.ninja;

import org.jramirezdfernandez.monolitonaruto.aldea.Aldea;
import org.jramirezdfernandez.monolitonaruto.jutsu.Jutsu;
import org.jramirezdfernandez.monolitonaruto.mision.Mision;
import org.jramirezdfernandez.monolitonaruto.mision.MisionDTO;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper
public interface NinjaMapper {

    default String mapAldea(Aldea aldea) {
        if (aldea == null) {
            return null;
        }
        return aldea.getName();
    }
    default List<String> mapJutsu(List<Jutsu> jutsus){

        return jutsus.stream().map(Jutsu::getName).toList();

    }


    NinjaDTO ninjaToNinjaDto(Ninja ninja);
}
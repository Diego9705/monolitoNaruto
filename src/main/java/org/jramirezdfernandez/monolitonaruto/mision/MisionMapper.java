package org.jramirezdfernandez.monolitonaruto.mision;


import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MisionMapper {

    default String map(Ninja ninja) {
        if (ninja == null) {
            return null;
        }
        return ninja.getName();
    }

    MisionDTO misionToMisionDTO(Mision mision);



}

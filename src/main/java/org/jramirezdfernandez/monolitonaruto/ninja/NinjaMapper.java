package org.jramirezdfernandez.monolitonaruto.ninja;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NinjaMapper {

    @Mapping(target = "aldea", source = "aldea") // Mapea el objeto completo
    @Mapping(target = "jutsus", expression = "java(ninja.getJutsus() != null ? ninja.getJutsus().stream().map(j -> j.getName()).toList() : java.util.Collections.emptyList())")
    NinjaDTO ninjaToNinjaDto(Ninja ninja);

    // Métodos default para el mapeo de listas de jutsu
    default List<String> mapJutsu(List<org.jramirezdfernandez.monolitonaruto.jutsu.Jutsu> jutsus) {
        if (jutsus == null) {
            return java.util.Collections.emptyList();
        }
        return jutsus.stream().map(jutsu -> jutsu.getName()).collect(Collectors.toList());
    }
}

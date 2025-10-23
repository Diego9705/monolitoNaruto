package org.jramirezdfernandez.monolitonaruto.aldea;

import org.jramirezdfernandez.monolitonaruto.jutsus.Jutsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AldeaRepository extends JpaRepository<Aldea, Long> {
}

package org.jramirezdfernandez.monolitonaruto.aldea;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AldeaRepository extends JpaRepository<Aldea, Long> {
}

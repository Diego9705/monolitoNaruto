package org.jramirezdfernandez.monolitonaruto.mision;

import org.jramirezdfernandez.monolitonaruto.ninja.Ninja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisionRepository extends JpaRepository<Mision, Long> {
}

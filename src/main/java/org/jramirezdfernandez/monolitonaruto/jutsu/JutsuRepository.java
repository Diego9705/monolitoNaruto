package org.jramirezdfernandez.monolitonaruto.jutsu;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JutsuRepository extends JpaRepository<Jutsu, Long> {
}

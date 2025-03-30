package br.com.Gadiel_S.repository;

import br.com.Gadiel_S.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}

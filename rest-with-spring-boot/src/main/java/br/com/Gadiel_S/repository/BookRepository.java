package br.com.Gadiel_S.repository;

import br.com.Gadiel_S.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

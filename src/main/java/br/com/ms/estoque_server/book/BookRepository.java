package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.template.TemplateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BookRepository extends TemplateRepository<Book>, JpaRepository<Book, Long> {

}
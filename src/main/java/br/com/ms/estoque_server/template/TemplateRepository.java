package br.com.ms.estoque_server.template;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository<T extends Template> {

    Optional<T> findByReferencia(Long id);

}

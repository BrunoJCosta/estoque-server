package br.com.ms.estoque_server.template;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository<T extends Template> {

    List<T> findAll();

    Optional<T> findByReferencia(Long id);

    Optional<T> findById(Long id);

    T saveAndFlush(T entity);

}

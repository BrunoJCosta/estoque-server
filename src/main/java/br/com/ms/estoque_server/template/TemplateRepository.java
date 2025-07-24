package br.com.ms.estoque_server.template;

import java.util.Optional;

public interface TemplateRepository<T> {

    Optional<T> findByReferencia(Long id);

}

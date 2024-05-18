package com.commons.microservices.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

//E, tipo generico, que representa una entity
public interface CommonService<E> {

    public Iterable<E> findAll();

    public Page<E> findAll(Pageable pageable);

    //Optional para controlar si la consulta viene null, vacia, etc.

    public Optional<E> findById(Long id);

    public E save(E alumno);

    public void deleteById(Long id);
}

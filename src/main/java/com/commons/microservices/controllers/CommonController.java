package com.commons.microservices.controllers;

import com.commons.microservices.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommonController<E, S extends CommonService<E>> {

    @Autowired
    protected S service;

    @GetMapping
    public ResponseEntity<?> getList() {
        return ResponseEntity.ok().body(service.findAll());
    }
    @GetMapping("/paginado")
    public ResponseEntity<?> getList(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<E> optionalE = service.findById(id);
        //Validar si existe el registro
        if(optionalE.isEmpty()) {
            //Build construye la respuesta con un body vacio
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(optionalE.get());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody E entity, BindingResult result) {
        if(result.hasErrors()){
            return this.validar(result);
        }
        E registerCreated = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<E> optionalE = service.findById(id);
        //Validar si existe el registro
        if(optionalE.isEmpty()) {
            //Build construye la respuesta con un body vacio
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Metodo para devolver mensajes de errores
    protected ResponseEntity<?> validar(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}

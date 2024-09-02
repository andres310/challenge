package com.ravn.challenge.service;

import java.util.List;
import java.util.NoSuchElementException;

public interface IGenericService<T, ID> {
	T save(T t);
    T update(T t);
    T findById(ID id) throws NoSuchElementException;
    List<T> findAll();
    void delete(ID id);
}

package com.ravn.challenge.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import com.ravn.challenge.repository.IGenericRepo;

public abstract class GenericService<T, ID> implements IGenericService<T, ID> {
	
	protected Class<T> className;
    protected abstract IGenericRepo<T, ID> getRepo();
    
    public GenericService(Class<T> className) {
    	this.className = className;
    }

    @Override
    @Transactional
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    @Transactional
    public T update(T t) {
        return getRepo().save(t);
    }

    @Override
    public T findById(ID id) throws NoSuchElementException {
        return getRepo().findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("%s with id %d not found", className.getName(), id)));
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    @Transactional
    public void delete(ID id) {
        getRepo().deleteById(id);
    }
}

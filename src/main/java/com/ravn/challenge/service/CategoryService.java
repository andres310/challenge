package com.ravn.challenge.service;

import org.springframework.stereotype.Service;

import com.ravn.challenge.entity.Category;
import com.ravn.challenge.repository.ICategoryRepository;
import com.ravn.challenge.repository.IGenericRepo;

@Service
public class CategoryService extends GenericService<Category, Long> {
	
	private final ICategoryRepository categoryRepository;

	public CategoryService(ICategoryRepository categoryRepository) {
		super(Category.class);
		this.categoryRepository = categoryRepository;
	}

	@Override
	protected IGenericRepo<Category, Long> getRepo() {
		return this.categoryRepository;
	}
}

package com.ravn.challenge.repository;

import java.util.Optional;

import com.ravn.challenge.entity.User;

/**
 * User persistence repository.
 * 
 * @author andres
 *
 */
public interface IUserRepository extends IGenericRepo<User, Long> {

	/**
	 * Finds a user by email wrapped in {@code Optional<User>}.
	 * 
	 * @param email
	 * @return User entity
	 */
	Optional<User> findOneByEmail(String email);
}

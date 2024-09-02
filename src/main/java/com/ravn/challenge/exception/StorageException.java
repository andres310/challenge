package com.ravn.challenge.exception;

/**
 * Customized exception to represent storage service exceptions.
 * 
 * @author andres
 */
public class StorageException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new instance of {@code StorageException} with a error message
     *
     * @param message
     */
	public StorageException(String message) {
		super(message);
	}

    /**
     * Creates a new instance of {@code StorageException} with a error message and a root cause.
     *
     * @param message Description.
     * @param cause   Root cause.
     */
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}

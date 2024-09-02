package com.ravn.challenge.exception;

/**
 * Customized exception for when a saved file cannot be found.
 * 
 * @author andres
 */
public class StorageFileNotFoundException extends StorageException {
	
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new instance of {@code StorageFileNotFoundException} with a error message.
     *
     * @param message 
     */
	public StorageFileNotFoundException(String message) {
		super(message);
	}

	/**
     * Creates a new instance of {@code StorageFileNotFoundException} with a error message and a root cause.
     *
     * @param message Description.
     * @param cause   Root cause.
     */
	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

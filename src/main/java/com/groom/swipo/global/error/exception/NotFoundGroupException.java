package com.groom.swipo.global.error.exception;

public abstract class NotFoundGroupException extends RuntimeException {
	public NotFoundGroupException(String message) {
		super(message);
	}
}

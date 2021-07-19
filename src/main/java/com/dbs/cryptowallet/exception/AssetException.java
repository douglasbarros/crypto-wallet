package com.dbs.cryptowallet.exception;

public class AssetException extends Exception {

	private static final long serialVersionUID = 4370884437829846307L;

	public AssetException(String message) {
		super(message);
	}

	public AssetException(String message, Throwable cause) {
		super(message, cause);
	}

}
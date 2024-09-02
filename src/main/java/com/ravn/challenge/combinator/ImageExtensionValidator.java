package com.ravn.challenge.combinator;

import java.util.function.Function;


public interface ImageExtensionValidator extends Function<String, Boolean> {
	
	static ImageExtensionValidator isPng() {
		return ext -> ext.contains("png");
	}
	
	static ImageExtensionValidator isJpeg() {
		return ext -> ext.contains("jpeg");
	}
	
	static ImageExtensionValidator isJpg() {
		return ext -> ext.contains("jpg");
	}
	
	default ImageExtensionValidator and(ImageExtensionValidator other) {
		return ext -> {
			Boolean result = this.apply(ext);
			return result == true ? other.apply(ext) : result;
		};
	}
	
	default ImageExtensionValidator or(ImageExtensionValidator other) {
		return ext -> {
			Boolean result = this.apply(ext);
			return result == true ? result : other.apply(ext);
		};
	}
}

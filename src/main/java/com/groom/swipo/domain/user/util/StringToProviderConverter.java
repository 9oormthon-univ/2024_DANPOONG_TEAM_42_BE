package com.groom.swipo.domain.user.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.groom.swipo.domain.user.entity.enums.Provider;
import com.groom.swipo.domain.user.exception.InvalidProviderException;

@Component
public class StringToProviderConverter implements Converter<String, Provider> {

	@Override
	public Provider convert(String source) {
		if (source.isEmpty()) {
			return null;
		}
		try {
			return Provider.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new InvalidProviderException();
		}
	}
}

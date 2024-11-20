package com.groom.swipo.domain.user.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.groom.swipo.domain.user.entity.enums.Telecom;
import com.groom.swipo.domain.user.exception.InvalidTelecomException;

@Component
public class StringToTelecomConverter implements Converter<String, Telecom> {

	@Override
	public Telecom convert(String source) {
		if (source.isEmpty()) {
			return null;
		}
		try {
			return Telecom.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new InvalidTelecomException();
		}
	}
}
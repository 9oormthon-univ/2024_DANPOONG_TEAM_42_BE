package com.groom.swipo.domain.user.util;

import java.time.LocalDateTime;
import java.util.Random;

import com.groom.swipo.domain.user.entity.VerificationCode;

public class VerificationCodeGenerator {
	private static final Integer EXPIRATION_TIME_IN_MINUTES = 3;

	public static VerificationCode generateVerificationCode(String to, LocalDateTime sentAt) {
		String code = generateRandomFourDigitNumber(); // 무작위 4자리 숫자 생성
		return VerificationCode.builder()
			.id(to) // 무작위 ID 사용
			.code(code)
			.expirationTimeInMinutes(EXPIRATION_TIME_IN_MINUTES)
			.build();
	}

	private static String generateRandomFourDigitNumber() {
		Random random = new Random();
		int number = random.nextInt(10000); // 0부터 9999까지의 숫자 생성
		return String.format("%04d", number); // 4자리로 포맷팅
	}
}
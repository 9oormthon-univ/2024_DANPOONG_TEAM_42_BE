package com.groom.swipo.domain.user.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode {
	@Id
	@Column(name = "verification_code_id")
	private String id;

	private String code;
	private Integer expirationTimeInMinutes;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createAt;

	@Builder
	private VerificationCode(
		String id,
		String code,
		Integer expirationTimeInMinutes,
		LocalDateTime createAt
	) {
		this.id = id;
		this.code = code;
		this.expirationTimeInMinutes = expirationTimeInMinutes;
		this.createAt = createAt;
	}

	public boolean isExpired(LocalDateTime verifiedAt) {
		LocalDateTime expiredAt = createAt.plusMinutes(expirationTimeInMinutes);
		return verifiedAt.isAfter(expiredAt);
	}

	public String generateCodeMessage() {
		String formattedExpiredAt = createAt
			.plusMinutes(expirationTimeInMinutes)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		// 메시지 포맷
		return String.format(
			"""
				[SWIPO 인증번호] 
				%s
				만료 기한 : %s
				""",
			code, formattedExpiredAt
		);
	}
}
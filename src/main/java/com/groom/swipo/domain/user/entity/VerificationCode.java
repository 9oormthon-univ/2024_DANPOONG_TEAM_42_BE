package com.groom.swipo.domain.user.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.groom.swipo.global.entity.BaseEntity;

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
public class VerificationCode extends BaseEntity {
	@Id
	@Column(name = "verification_code_id")
	private String id;

	private String code;
	private Integer expirationTimeInMinutes;

	@Builder
	private VerificationCode(
		String id,
		String code,
		Integer expirationTimeInMinutes
	) {
		this.id = id;
		this.code = code;
		this.expirationTimeInMinutes = expirationTimeInMinutes;
	}

	public boolean isExpired(LocalDateTime verifiedAt) {
		LocalDateTime expiredAt = getCreatedAt().plusMinutes(expirationTimeInMinutes);
		return verifiedAt.isAfter(expiredAt);
	}

	public String generateCodeMessage() {
		// 메시지 포맷
		return String.format(
			"""
				[SWIPO 인증번호] 
				%s
				""",
			code
		);
	}
}
package com.groom.swipo.domain.user.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.groom.swipo.domain.user.entity.VerificationCode;
import com.groom.swipo.domain.user.exception.CodeTimeoutException;
import com.groom.swipo.domain.user.exception.PhoneNotFoundException;
import com.groom.swipo.domain.user.exception.PhoneVerificationCompletionException;
import com.groom.swipo.domain.user.repository.VerificationCodeRepository;
import com.groom.swipo.domain.user.util.VerificationCodeGenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {
	private final VerificationCodeRepository verificationCodeRepository;
	private DefaultMessageService messageService;

	@Value("${sms.api-key}")
	private String apiKey;
	@Value("${sms.api-secret}")
	private String apiSecret;
	@Value("${sms.provider}")
	private String smsProvider;
	@Value("${sms.sender}")
	private String smsSender;

	@PostConstruct
	public void init() {
		messageService = NurigoApp.INSTANCE.initialize(
			apiKey,
			apiSecret,
			smsProvider
		);
	}

	public void sendVerificationMessage(String to, LocalDateTime sentAt) {
		Message message = new Message();
		message.setFrom(smsSender);
		message.setTo(to);

		VerificationCode verificationCode = VerificationCodeGenerator
			.generateVerificationCode(to, sentAt);
		VerificationCode savedCode = verificationCodeRepository.save(verificationCode);
		System.out.println("Saved Verification Code ID: " + savedCode);

		String text = verificationCode.generateCodeMessage();
		message.setText(text);

		messageService.sendOne(new SingleMessageSendingRequest(message));
	}

	public void verifyCode(String phone, String code, LocalDateTime verifiedAt) {
		VerificationCode verificationCode = verificationCodeRepository.findById(phone)
			.orElseThrow(PhoneNotFoundException::new);

		if (!Objects.equals(verificationCode.getCode(), code)) {
			throw new PhoneVerificationCompletionException();
		}
		if (verificationCode.isExpired(verifiedAt)) {
			throw new CodeTimeoutException();
		}

		verificationCodeRepository.delete(verificationCode);
	}
}
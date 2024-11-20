package com.groom.swipo.domain.user.service;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.swipo.domain.user.dto.request.RegisterUserRequest;
import com.groom.swipo.domain.user.dto.response.RegisterUserResponse;
import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.domain.user.entity.enums.Provider;
import com.groom.swipo.domain.user.entity.enums.Telecom;
import com.groom.swipo.domain.user.exception.DuplicatePhoneNumberException;
import com.groom.swipo.domain.user.exception.OAuthRequestException;
import com.groom.swipo.domain.user.exception.UserAlreadyExistsException;
import com.groom.swipo.domain.user.repository.UserRepository;
import com.groom.swipo.global.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	private final ConversionService conversionService;

	private final TokenProvider tokenProvider;

	@Transactional
	public RegisterUserResponse registerUser(RegisterUserRequest request) {
		if (request.provider() == null || request.providerId() == null) {
			throw new OAuthRequestException();
		}

		// convert
		Provider provider = conversionService.convert(request.provider(), Provider.class);
		Telecom telecom = conversionService.convert(request.telecom(), Telecom.class);

		if (userRepository.findByProviderAndProviderId(provider, request.providerId()).isPresent()) {  // 이미 회원가입 되어있는지 검증
			throw new UserAlreadyExistsException();
		} else if (userRepository.existsByPhone(request.phone())) {                                    // 중복 휴대폰 검증
			throw new DuplicatePhoneNumberException();
		}

		User savedUser = saveUser(request, provider, telecom);

		String accessToken = tokenProvider.createAccessToken(savedUser);
		String refreshToken = tokenProvider.createRefreshToken(savedUser);

		return new RegisterUserResponse(savedUser.getId(), accessToken, refreshToken);
	}

	// 유저 정보 저장
	private User saveUser(RegisterUserRequest request, Provider provider, Telecom telecom) {
		User user = User.builder()
			.provider(provider)
			.providerId(request.providerId())
			.name(request.name())
			.address(request.address())
			.birth(request.birth())
			.telecom(telecom)
			.phone(request.phone())
			.password(request.pwd())
			.isMarketing(request.isMarket())
			.imageUrl("default 이미지")
			.isOpenbank(false)
			.build();

		return userRepository.save(user);
	}
}

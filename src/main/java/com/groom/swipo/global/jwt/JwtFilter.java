package com.groom.swipo.global.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.swipo.domain.auth.exception.InvalidTokenException;
import com.groom.swipo.global.template.ResTemplate;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

	private final TokenProvider tokenProvider;
	private final ObjectMapper objectMapper = new ObjectMapper()
		.setSerializationInclusion(JsonInclude.Include.NON_NULL); // data가 null 값으로 자꾸 들어가서 직렬화로 제외

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String token = tokenProvider.resolveToken(httpRequest);

		try {
			if (StringUtils.hasText(token)) {
				tokenProvider.validateToken(token);
				Authentication authentication = tokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
		} catch (InvalidTokenException e) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType("application/json");
			httpResponse.setCharacterEncoding("UTF-8");
			ResTemplate<Void> errorResponse = new ResTemplate<>(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
			httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
			httpResponse.getWriter().flush();
		}
	}
}
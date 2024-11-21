package com.groom.swipo.domain.store.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groom.swipo.domain.store.service.WishlistService;
import com.groom.swipo.global.template.ResTemplate;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/store")
public class StoreController {

	private final WishlistService wishlistService;

	@PostMapping("/wish")
	public ResTemplate<Void> updateWish(@RequestParam(name = "storeId") Long storeId,
		@RequestParam(name = "isWish") boolean isWish, Principal principal) {
		wishlistService.updateWish(storeId, isWish, principal);
		return new ResTemplate<>(HttpStatus.NO_CONTENT, "관심 등록/해제 성공");
	}
}

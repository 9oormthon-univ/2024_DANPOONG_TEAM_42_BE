package com.groom.swipo.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.groom.swipo.domain.payment.entity.Pay;
import com.groom.swipo.domain.payment.entity.PayChargelist;
import com.groom.swipo.domain.point.entity.Card;
import com.groom.swipo.domain.point.entity.MyPiece;
import com.groom.swipo.domain.store.entity.Reviews;
import com.groom.swipo.domain.store.entity.Wishlist;
import com.groom.swipo.domain.user.entity.enums.Provider;
import com.groom.swipo.domain.user.entity.enums.Telecom;
import com.groom.swipo.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private Provider provider; // 소셜 로그인 제공자

	@Column(nullable = false, unique = true)
	private String providerId; // 소셜 로그인 제공자 고유번호

	@Column(nullable = false)
	private String name; // 사용자 이름

	@Column(nullable = false)
	private String address; // 사용자 주소

	@Column(nullable = false)
	private String birth; // 생년월일

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Telecom telecom; // 사용자 통신사

	@Column(nullable = false, unique = true)
	private String phone; // 사용자 전화번호

	@Column(nullable = false)
	private String password; // 사용자 간편비밀번호

	@Column(nullable = false)
	private Boolean isMarketing; // 사용자 마켓팅 여부

	private String imageUrl; // 사용자 프로필 이미지 URL

	@Column(nullable = false)
	@ColumnDefault("false")
	private Boolean isOpenbank; // 사용자 오픈뱅킹 등록여부

	// 밑에 연관관계 적어두기
	// 페이 충전내역
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<PayChargelist> payChargelists = new ArrayList<>();

	// 보유조각
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<MyPiece> myPieces = new ArrayList<>();

	// 리뷰
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Reviews> reviews = new ArrayList<>();

	// 관심
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Wishlist> wishlists = new ArrayList<>();

	// 카드
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Card> Cards = new ArrayList<>();

	// 페이
	@OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
	private Pay pays;

	// 기타
	@Builder
	private User(Provider provider,
		String providerId,
		String name,
		String address,
		Telecom telecom,
		String phone,
		String password,
		Boolean isMarketing,
		String imageUrl,
		Boolean isOpenbank
	) {
		this.provider = provider;
		this.providerId = providerId;
		this.name = name;
		this.address = address;
		this.telecom = telecom;
		this.phone = phone;
		this.password = password;
		this.isMarketing = isMarketing;
		this.imageUrl = imageUrl;
		this.isOpenbank = isOpenbank;
	}
}
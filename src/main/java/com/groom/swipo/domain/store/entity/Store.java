package com.groom.swipo.domain.store.entity;

import java.util.ArrayList;
import java.util.List;

import com.groom.swipo.domain.payment.entity.Paylist;
import com.groom.swipo.domain.point.entity.Cardlist;
import com.groom.swipo.domain.point.entity.MyPiece;
import com.groom.swipo.domain.store.entity.enums.StoreCategory;
import com.groom.swipo.domain.store.entity.enums.StoreType;
import com.groom.swipo.global.common.enums.Area;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StoreType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StoreCategory category;

	private String address;

	@Column(nullable = false)
	private Double latitude;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Area area;

	@Column(nullable = false)
	private Double longitude;

	@Column(nullable = false)
	private Integer percent;

	// 연관관계
	// 페이 이용내역
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Paylist> paylists = new ArrayList<>();

	// 보유 조각
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MyPiece> myPieces = new ArrayList<>();

	// 리뷰
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reviews> reviews = new ArrayList<>();

	// 위시리스트
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Wishlist> wishlists = new ArrayList<>();

	// 가게 이미지
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoreImage> storeImages = new ArrayList<>();

	// 카드 이용내역
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cardlist> cardlists = new ArrayList<>();

	// 기타
	@Builder
	private Store(String name, StoreType type, StoreCategory category, String address, Double latitude, Area area,
		Double longitude, Integer percent) {
		this.name = name;
		this.type = type;
		this.category = category;
		this.address = address;
		this.latitude = latitude;
		this.area = area;
		this.longitude = longitude;
		this.percent = percent;
	}
}

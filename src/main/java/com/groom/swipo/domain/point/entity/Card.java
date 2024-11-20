package com.groom.swipo.domain.point.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.global.common.enums.Area;
import com.groom.swipo.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "card_id")
	private Long id;

	@Column(nullable = false)
	private String number;

	private String contents;

	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer totalPoint;

	@Column(nullable = false)
	private String customeImage;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Area area;

	// 유저 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// 카드 이용내역
	@OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST)
	private List<Cardlist> cardlists = new ArrayList<>();

	@Builder
	private Card(String number, String contents, Integer totalPoint, String customeImage, Area area) {
		this.number = number;
		this.contents = contents;
		this.totalPoint = totalPoint;
		this.customeImage = customeImage;
		this.area = area;
	}
}

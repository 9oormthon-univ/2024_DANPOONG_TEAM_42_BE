package com.groom.swipo.domain.store.entity;

import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reviews extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reviews_id")
	private Long id;

	@Column(nullable = false)
	private Double star;

	@Column(nullable = false)
	private String comment;

	// 유저 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// 가게 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Builder
	private Reviews(Double star, String comment, User user, Store store) {
		this.star = star;
		this.comment = comment;
		this.user = user;
		this.store = store;
	}
}

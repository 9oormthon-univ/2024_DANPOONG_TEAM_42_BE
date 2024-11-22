package com.groom.swipo.domain.point.entity;

import com.groom.swipo.domain.store.entity.Store;
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
public class MyPiece extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "my_piece_id")
	private Long id;

	@Column(nullable = false)
	private Boolean isDeleted;

	// 유저 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// 조각 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "piece_id")
	private Piece piece;

	// 가게 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Builder
	private MyPiece(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
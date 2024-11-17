package com.groom.swipo.domain.payment.entity;

import com.groom.swipo.domain.store.entity.Store;
import com.groom.swipo.global.common.enums.Area;
import com.groom.swipo.global.entity.BaseEntity;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Paylist extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "paylist_id")
	private Long id;

	@Column(nullable = false)
	private long value;

	// 페이 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pay_id")
	private Pay pay;

	// 가게 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Builder
	private Paylist(long value) {
		this.value = value;
	}
}
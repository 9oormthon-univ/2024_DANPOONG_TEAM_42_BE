package com.groom.swipo.domain.payment.entity;

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
public class PayChargeList extends BaseEntity {
	@Id
	@Column(name = "pay_charge_list_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name; // 충전명

	@Column(nullable = false)
	private Integer price; // 충전 금액

	private String impUid; // 포트원 고유 식별자

	@Column(nullable = false)
	private String merchantUid; // 고유 식별자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	private PayChargeList(String name, Integer price, String impUid, String merchantUid) {
		this.name = name;
		this.price = price;
		this.impUid = impUid;
		this.merchantUid = merchantUid;
	}
}
package com.groom.swipo.domain.payment.entity;

import java.util.ArrayList;
import java.util.List;

import com.groom.swipo.domain.user.entity.User;
import com.groom.swipo.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pay extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pay_id")
	private Long id;

	@Column(nullable = false)
	private Integer totalPay;

	// 유저 id
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;

	// 페이 내역
	@OneToMany(mappedBy = "pay", cascade = CascadeType.PERSIST)
	private List<Paylist> paylists = new ArrayList<>();

	@Builder
	private Pay(Integer totalPay, User user) {
		this.totalPay = totalPay;
		this.user = user;
	}

	public void updatePay(Integer payIncrement) {
		this.totalPay += payIncrement;
	}
}

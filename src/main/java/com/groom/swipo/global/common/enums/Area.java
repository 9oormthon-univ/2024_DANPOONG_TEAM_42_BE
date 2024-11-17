package com.groom.swipo.global.common.enums;

public enum Area {
	CODE_02("02"), // 서울
	CODE_05("05"), // 경남
	CODE_031("031"), // 경기도
	CODE_032("032"), // 인천
	CODE_033("033"), // 강원
	CODE_041("041"), // 충남
	CODE_042("042"), // 대전
	CODE_043("043"), // 충북
	CODE_044("044"), // 세종
	CODE_051("051"), // 부산
	CODE_052("052"), // 울산
	CODE_053("053"), // 대구
	CODE_054("054"), // 경북
	CODE_061("061"), // 전남
	CODE_062("062"), // 광주
	CODE_063("063"), // 전북
	CODE_064("064"); // 제주


	private final String code;

	Area(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
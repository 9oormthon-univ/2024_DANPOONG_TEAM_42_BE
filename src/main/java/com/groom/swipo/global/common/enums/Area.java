package com.groom.swipo.global.common.enums;

import java.util.Arrays;
import com.groom.swipo.domain.point.exception.InvalidRegionException;

public enum Area {
	CODE_02("02", "서울"),
	CODE_031("031", "경기"),
	CODE_032("032", "인천"),
	CODE_033("033", "강원"),
	CODE_041("041", "충남"),
	CODE_042("042", "대전"),
	CODE_043("043", "충북"),
	CODE_044("044", "세종"),
	CODE_051("051", "부산"),
	CODE_052("052", "울산"),
	CODE_053("053", "대구"),
	CODE_054("054", "경북"),
	CODE_055("055", "경남"),
	CODE_061("061", "전남"),
	CODE_062("062", "광주"),
	CODE_063("063", "전북"),
	CODE_064("064", "제주");

	private final String code;
	private final String regionName;

	Area(String code, String regionName) {
		this.code = code;
		this.regionName = regionName;
	}

	public String getCode() {
		return code;
	}

	public String getRegionName() {
		return regionName;
	}

	// 지역명을 기준으로 Area 반환
	public static Area fromRegionName(String regionName) {
		return Arrays.stream(values())
			.filter(area -> area.getRegionName().equals(regionName))
			.findFirst()
			.orElseThrow(InvalidRegionException::new); // 여기서 에러 핸들링하는게 맞겠죠..? 클린하겠죠? ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
	}
}

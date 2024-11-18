#!/usr/bin/env bash

# 쉬고 있는 profile 찾기: real1이 사용 중이면 real2가 쉬고 있고 반대면 real1이 쉬고 있음

function find_idle_profile() {
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" https://duoh.site/profile) # 현재 Nginx가 바라보고 있는 스프링 부트가 정상적으로 수행 중인지 확인하고 응답값으로 상태코드를 전달받음

    if [ ${RESPONSE_CODE} -ge 400 ] # 400번대 이상의 오류일 경우 real2를 사용
    then
      CURRENT_PROFILE=real2
    else
      CURRENT_PROFILE=$(curl -s https://duoh.site/profile)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}" # bash 스크립트는 반환 기능이 없기 때문에 echo로 값을 출력하고 클라이언트에서 그 값을 잡아서 사용
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port() {
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == real1 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}

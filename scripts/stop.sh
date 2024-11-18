#!/usr/bin/env bash

ABSPATH=$(readlink -f $0) # 현재 stop.sh가 속해 있는 경로를 찾습니다.

ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh # 일종의 import 구문으로 stop.sh에서도 profile.sh의 function을 사용할 수 있게 합니다.

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동 중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -9 $IDLE_PID"
  kill -9 ${IDLE_PID}
  sleep 5
fi

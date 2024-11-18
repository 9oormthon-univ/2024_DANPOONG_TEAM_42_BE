#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app

echo "> Build 파일을 복사합니다."

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한을 부여합니다."

chmod +x $JAR_NAME

IDLE_PROFILE=$(find_idle_profile)

echo "> 새 애플리케이션을 $IDLE_PROFILE 로 실행합니다."

# 설정 파일의 위치를 지정하고 active profile을 통해 구동될 포트를 지정합니다.
nohup java -jar \
-Dspring.config.location=$REPOSITORY/config/application.yml,\
$REPOSITORY/config/application-prod.yml,\
$REPOSITORY/config/application-$IDLE_PROFILE.yml \
-Dspring.profiles.active=$IDLE_PROFILE,prod \
$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

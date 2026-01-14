#!/bin/bash

echo "=========================================="
echo "배포 시작"
echo "=========================================="

echo ">>> Git Pull"
git pull origin main

if [ $? -ne 0 ]; then
    echo "Git Pull 실패"
    exit 1
fi

echo ">>> 프로젝트 빌드 시작"
./gradlew clean build

if [ $? -ne 0 ]; then
    echo "빌드 실패"
    exit 1
fi

echo ">>> 실행 중인 애플리케이션 확인"
CURRENT_PID=$(pgrep -f roomescape)

if [ -z "$CURRENT_PID" ]; then
    echo ">>> 실행 중인 애플리케이션이 없습니다."
else
    echo ">>> 애플리케이션 종료 (PID: $CURRENT_PID)"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo ">>> 새 애플리케이션 실행"
nohup java -jar build/libs/roomescape-0.0.1-SNAPSHOT.jar > application.log 2>&1 &

sleep 3

NEW_PID=$(pgrep -f roomescape)
echo ">>> 배포 완료 (PID: $NEW_PID)"

echo "=========================================="

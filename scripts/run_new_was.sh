# CURRENT_PORT: 현재 Nginx 가 바라보고 있는 포트 번호를 service-url.inc 파일에서 읽어옵니다.
CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
# TARGET_PORT : 새로운 서버를 배포할 포트를 현재 Nginx 가 바라보고 있지 않은 포트로 설정합니다
TARGET_PORT=0

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "[$NOW_TIME] No WAS is connected to nginx"
fi
# TARGET_PID : 새로운 서버를 배포할 포트에서 현재 실행중인 프로세스 아이디입니다. 새로운 서버를 배포하기 위해 기존에 실행중인 프로세스가 있다면 종료합니다.
TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "Kill WAS running at ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi

nohup java -jar -Dserver.port=${TARGET_PORT} /home/ubuntu/app/*.jar
echo "Now new WAS runs at ${TARGET_PORT}."

exit 0
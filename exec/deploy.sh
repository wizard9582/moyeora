REPOSITORY=/home/ubuntu/PJT
PROJECT_NAME=S05P13A306
BACKEND_PROJECT=backend
FRONT_PROJECT=frontend

cd $REPOSITORY/$PROJECT_NAME

echo "> git pull"
#git fetch --all
#git reset --hard origin/develop
git pull origin develop
#mkdir $REPOSITORY/$PROJECT_NAME/$BACKEND_PROJECT/src/main/resources/keystore
#cp ../*.P12 $REPOSITORY/$PROJECT_NAME/$BACKEND_PROJECT/src/main/resources/keystore/

echo ">front 빌드"
cd $FRONT_PROJECT
#포트 서버에 맞게 치환
sed -i 's/localhost:8443/i5a306.p.ssafy.io:443/g' ./src/common/lib/webSocket.js
#cat ./src/common/lib/webSocket.js | head -n 2

npm run build

echo "> 프로젝트 빌드"
cd $REPOSITORY/$PROJECT_NAME/$BACKEND_PROJECT
sudo chmod 755 gradlew
./gradlew build

echo "> $REPOSITORY 디렉터리로 이동"
cd $REPOSITORY

echo "> 배포 파일 복사"
cp $REPOSITORY/$PROJECT_NAME/$BACKEND_PROJECT/build/libs/*.jar $REPOSITORY/

echo "> 구동 중인 애플리케이션 PID 검색"
CURRENT_PID=$(pgrep -fl 'ssfay-fifth*' | grep jar | awk '{ print $1 }')

echo "> 구동 중인 애플리케이션 PID : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)
echo "> 새 애플리케이션($JAR_NAME) 배포"
nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &

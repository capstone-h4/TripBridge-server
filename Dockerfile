FROM openjdk:17

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar tripbridge-server-0.0.1-SNAPSHOT.jar

# 사용할 포트
EXPOSE 8080

# 로그 디렉토리 생성 및 권한 부여
RUN mkdir -p ./logs && chmod 755 ./logs

# 컨테이너 실행 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "tripbridge-server-0.0.1-SNAPSHOT.jar"]

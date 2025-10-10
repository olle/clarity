JAVA_HOME=$(shell unset JAVA_HOME; /usr/libexec/java_home -v 21)
MVN:=./mvnw
NPM:=./node/npm

.PHONY: verify v
verify v: frontend
	${MVN} verify

.PHONY: frontend fe
frontend fe: ${NPM}
	${NPM} -C frontend install
	${NPM} -C frontend run build

.PHONY: dev run-backend run-frontend
dev:
	${MAKE} -j2 run-backend run-frontend

run-backend:
	SPRING_PROFILES_ACTIVE=dev ${MVN} spring-boot:run

run-frontend: ${NPM}
	${NPM} -C frontend run dev

${NPM}:
	${MVN} initialize

.PHONY: tidy format pretty f
tidy pretty format f:
	${MVN} formatter:format

.PHONY: up
up:
	docker compose up -d

.PHONY: down
down:
	docker compose down

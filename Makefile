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

.PHONY: run r run-all run-backend run-frontend
run r:
	${MAKE} -j2 run-backend run-frontend

run-backend:
	${MVN} spring-boot:run

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

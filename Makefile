JAVA_HOME=$(shell unset JAVA_HOME; /usr/libexec/java_home -v 21)
MVN:=./mvnw
NPM:=./node/npm

.PHONY: verify v
verify v: backend frontend

.PHONY: backend be
backend be: ${MVN}
	${MVN} verify

.PHONY: frontend fe
frontend fe: ${NPM}
	${NPM} -C frontend install
	${NPM} -C frontend audit
	${NPM} -C frontend outdated
	${NPM} -C frontend run build

.PHONY: clean c
clean c:
	${MVN} clean
	rm -rf src/main/resources/static/*

.PHONY: run
run:
	${MAKE} -j2 run-backend run-frontend

.PHONY: run-backend run-be r-b
run-backend run-be r-be:
	SPRING_PROFILES_ACTIVE=dev ${MVN} spring-boot:run

.PHONY: run-frontend run-fe r-f
run-frontend run-fe r-fe: ${NPM}
	${NPM} -C frontend run dev

${NPM}:
	${MVN} initialize

${MVN}:
	@echo "Please install the maven wrapper."
	@exit 1

.PHONY: tidy spotless pretty format f
tidy spotless pretty format f:
	${MVN} spotless:apply

.PHONY: up
up:
	docker compose up -d

.PHONY: down
down:
	docker compose down

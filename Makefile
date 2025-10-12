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
	${NPM} -C frontend run build

.PHONY: dev run-backend r-be run-frontend r-fe
dev:
	${MAKE} -j2 run-backend run-frontend

run-backend r-be:
	SPRING_PROFILES_ACTIVE=dev ${MVN} spring-boot:run

run-frontend r-fe: ${NPM}
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

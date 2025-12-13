ifeq ($(shell command -v podman 2> /dev/null),)
    CNTR := docker
else
    CNTR := podman
endif

JAVA_HOME=$(shell unset JAVA_HOME; /usr/libexec/java_home -v 25)
MVN:=./mvnw
NPM:=./node/npm

.PHONY: verify v
verify v: backend frontend outdated

.PHONY: backend be
backend be: ${MVN}
	${MVN} verify

.PHONY: frontend fe
frontend fe: ${NPM}
	${NPM} -C frontend ci
	${NPM} -C frontend audit
	${NPM} -C frontend run build

.PHONY: outdated o
outdated o:
	${NPM} -C frontend outdated
	${MVN} versions:display-property-updates

.PHONY: clean c
clean c:
	${MVN} clean
	rm -rf target/classes/static/*

.PHONY: offline
offline:
	${NPM} -C frontend run build
	${MAKE} -j2 run-backend run-frontend

.PHONY: run
run: target/classes/static/index.html
	${MAKE} -j2 run-backend run-frontend

target/classes/static/index.html: frontend

.PHONY: demo d
demo d: target/classes/static/index.html
	${MAKE} -j2 run-demo-backend run-frontend

.PHONY: run-backend run-be r-b
run-backend run-be r-be:
	SPRING_PROFILES_ACTIVE=dev ${MVN} spring-boot:run

.PHONY: run-demo-backend
run-demo-backend:
	SPRING_PROFILES_ACTIVE=demo,dev ${MVN} spring-boot:run

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
	${MVN} com.github.ekryd.sortpom:sortpom-maven-plugin:sort

.PHONY: up
up:
	${CNTR} compose up -d

.PHONY: down
down:
	${CNTR} compose down

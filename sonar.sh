#!/usr/bin/env bash

#mvn test

mvn sonar:sonar \
  -Dsonar.projectKey=mermaid \
  -Dsonar.projectName="美人鱼:视觉冰箱客服" \
  -Dsonar.host.url=http://sonar.dev.uboxol.com \
  -Dsonar.login=0c32d95baeda80f7c53fda5cfdc0fce97907a423
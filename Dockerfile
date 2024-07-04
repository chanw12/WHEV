name: Deploy to Fly.io

on:
  push:
    paths:
      - settings.gradle
      - build.gradle
      - src/**
      - fly.toml
      - Dockerfile
      - .github/workflows/deploy.yml
    branches:
      - main

jobs:
  deploy:
    name: Deploy app
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          distribution: 'temurin'
          java-version: '21'
      - name: application-secret.yml 생성
        env:
          APPLICATION_SECRET: ${{ secrets.APPLICATION_SECRET_YML }}
        run: echo "$APPLICATION_SECRET" > src/main/resources/application-secret.yml
      - name: Build without tests
        run: ./gradlew build -x test
      - uses: superfly/flyctl-actions/setup-flyctl@master
      - name: Install Fly.io CLI
        run: |
          curl -L https://fly.io/install.sh | sh
          export FLYCTL_INSTALL="/home/runner/.fly"
          export PATH="$FLYCTL_INSTALL/bin:$PATH"
          fly version
      - name: Deploy to Fly.io
        env:
          FLY_API_TOKEN: ${{ secrets.FLY_API_TOKEN }}
        run: |
          flyctl deploy --config fly.toml --remote-only

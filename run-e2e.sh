#!/bin/bash
# Ce script permet d'exécuter le serveur backend en arrière plan avant de lancer les tests e2e avec Cypress
# ATTENTION! Vous ne pouvez pas modifier le script `run-e2e.sh`. Il correspond au script utilisé pour la correction.

# Arrêter le script en cas d'erreur
set -e

echo "Starting backend server with Maven..."
mvn exec:java -Dexec.args=TEST &
MAVEN_PID=$!

cd webapp
echo "Waiting for backend server to start..."
npx wait-on http://localhost:8181/health

echo "Running Cypress tests..."
npm run test:e2e:dev

echo "Stopping backend server..."
kill $MAVEN_PID

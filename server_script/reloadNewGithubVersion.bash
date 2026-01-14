#!/bin/bash
cd dyeTrack
docker-compose down
cd ..
echo "📌 Arrêt du conteneur app (s'il existe)..."
docker stop dyetrack_app_1 2>/dev/null || true
docker rm -f dyetrack_app_1 2>/dev/null || true

echo "📌 Suppression de l'image Docker app..."
docker image rm -f molinierhugo/api-dyentrack:latest 2>/dev/null || true

echo "📌 Suppression éventuelle du dossier dyeTrack..."
rm -rf dyeTrack

echo "📌 Clonage du dépôt Git..."
git clone https://github.com/HugoMolinier/dyeTrack.git

echo "📌 Copie du fichier .env..."
cp .env dyeTrack/.env

echo "📌 Build et lancement du conteneur app..."
cd dyeTrack
docker-compose up -d --build app
docker-compose up -d
echo "✅ Déploiement terminé !"

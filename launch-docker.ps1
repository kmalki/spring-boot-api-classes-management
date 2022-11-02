docker build -t kmalki/myapp:latest -f docker\Dockerfile .
docker compose -f .\docker\docker-compose.yml up -d
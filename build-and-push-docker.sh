#!/bin/bash
# Script to build and push Docker image for chatalgo-api

# Configuration
DOCKER_REGISTRY="your-registry.com"  # Change this to your Docker registry
IMAGE_NAME="chatalgo-api"
IMAGE_TAG=$(git rev-parse --short HEAD)  # Use git commit hash as tag

# Build the Docker image
echo "Building Docker image: ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
docker build -t ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} -t ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest .

# Push the Docker image to the registry
echo "Pushing Docker image to registry..."
docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest

echo "Docker image built and pushed successfully:"
echo "  ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
echo "  ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest"

# Output the Helm command to deploy or upgrade
echo ""
echo "To deploy to development environment, run:"
echo "helm upgrade --install chatalgo-api ./helm/chatalgo-api \\"
echo "  --values ./helm/chatalgo-api/values-dev.yaml \\"
echo "  --set image.repository=${DOCKER_REGISTRY}/${IMAGE_NAME} \\"
echo "  --set image.tag=${IMAGE_TAG} \\"
echo "  --namespace your-namespace \\"
echo "  --create-namespace"

echo ""
echo "To deploy to production environment, run:"
echo "helm upgrade --install chatalgo-api ./helm/chatalgo-api \\"
echo "  --values ./helm/chatalgo-api/values-prod.yaml \\"
echo "  --set image.repository=${DOCKER_REGISTRY}/${IMAGE_NAME} \\"
echo "  --set image.tag=${IMAGE_TAG} \\"
echo "  --namespace your-namespace \\"
echo "  --create-namespace"

#!/bin/bash
# Script to build and push Docker image for chatalgo-api

# Configuration
NAMESPACE=${NAMESPACE:-"default"}
RELEASE_NAME=${RELEASE_NAME:-"chatalgo"}
IMAGE_NAME=${IMAGE_NAME:-"chatalgo-api"}
IMAGE_TAG=$(git rev-parse --short HEAD)  # Use git commit hash as tag

# Push the Docker image to the registry
echo "Pushing Docker image to registry..."
docker buildx build -t ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} -f Dockerfile --platform linux/amd64,linux/arm64 --push .

echo "Docker image built and pushed successfully:"
echo "  ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
echo "  ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest"

# Output the Helm command to deploy or upgrade
echo ""
echo "To deploy to development environment, run:"
echo "helm upgrade --install ${RELEASE_NAME} ./helm/${RELEASE_NAME} \\"
echo "  --values ./helm/${RELEASE_NAME}/values-dev.yaml \\"
echo "  --set image.repository=${DOCKER_REGISTRY}/${IMAGE_NAME} \\"
echo "  --set image.tag=${IMAGE_TAG} \\"
echo "  --namespace ${NAMESPACE} \\"
echo "  --create-namespace"

echo ""
echo "To deploy to production environment, run:"
echo "helm upgrade --install ${RELEASE_NAME} ./helm/${RELEASE_NAME} \\"
echo "  --values ./helm/${RELEASE_NAME}/values-prod.yaml \\"
echo "  --set image.repository=${DOCKER_REGISTRY}/${IMAGE_NAME} \\"
echo "  --set image.tag=${IMAGE_TAG} \\"
echo "  --namespace ${NAMESPACE} \\"
echo "  --create-namespace"

#!/bin/bash
set -e

# Default values
NAMESPACE=${NAMESPACE:-"default"}
RELEASE_NAME=${RELEASE_NAME:-"chatalgo-api"}
CHART_DIR="./deploy"
VALUES_FILE=""
DOCKER_REGISTRY=${DOCKER_REGISTRY:-"webdev0594"}
IMAGE_TAG=${IMAGE_TAG:-$(git rev-parse --short HEAD)}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --namespace)
      NAMESPACE="$2"
      shift 2
      ;;
    --release-name)
      RELEASE_NAME="$2"
      shift 2
      ;;
    --values)
      VALUES_FILE="$2"
      shift 2
      ;;
    --registry)
      DOCKER_REGISTRY="$2"
      shift 2
      ;;
    --tag)
      IMAGE_TAG="$2"
      shift 2
      ;;
    --help)
      echo "Usage: $0 [options]"
      echo "Options:"
      echo "  --namespace NAMESPACE   Kubernetes namespace (default: default)"
      echo "  --release-name NAME     Helm release name (default: chatalgo)"
      echo "  --values FILE           Values file to use"
      echo "  --registry REGISTRY     Docker registry (default: my-registry.example.com)"
      echo "  --tag TAG               Image tag (default: git short commit hash)"
      echo "  --help                  Show this help message"
      exit 0
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# Build and push Docker image
echo "Building Docker image..."
docker buildx build -t ${DOCKER_REGISTRY}/chatalgo-api:${IMAGE_TAG} -f Dockerfile --platform linux/amd64,linux/arm64 --push .

# Package Helm chart
echo "Packaging Helm chart..."
helm package $CHART_DIR --destination ./helm/packages

# Deploy with Helm
echo "Deploying to Kubernetes..."
HELM_CMD="helm upgrade --install $RELEASE_NAME ./deploy --namespace $NAMESPACE --set image.repository=${DOCKER_REGISTRY}/chatalgo --set image.tag=${IMAGE_TAG}"

if [ -n "$VALUES_FILE" ]; then
  HELM_CMD="$HELM_CMD --values $VALUES_FILE"
fi

echo "Executing: $HELM_CMD"
eval $HELM_CMD

echo "Deployment completed!"
echo "Use 'kubectl get pods -n $NAMESPACE' to check the status of your pods." 

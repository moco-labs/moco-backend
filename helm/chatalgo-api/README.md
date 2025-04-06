# Chatalgo API Helm Chart

이 Helm Chart는 Chatalgo API 애플리케이션을 Kubernetes 클러스터에 배포하기 위한 것입니다.

## 사전 요구 사항

- Kubernetes 클러스터(버전 1.19 이상)
- Helm 3.0 이상
- kubectl이 구성된 환경

## Helm Chart 설치 방법

### 1. 설치 전 환경 설정

MongoDB와 OpenAI API Key를 위한 Secret을 생성합니다:

```bash
# MongoDB 비밀번호 생성
kubectl create secret generic chatalgo-mongodb-secret \
  --from-literal=mongodb-uri="mongodb+srv://otter:<password>@chatalgo.e3jtoia.mongodb.net/" \
  --namespace <namespace>

# OpenAI API Key 생성
kubectl create secret generic chatalgo-openai-secret \
  --from-literal=api-key="<your-openai-api-key>" \
  --namespace <namespace>
```

### 2. Helm Chart 설치

```bash
# 개발 환경
helm install chatalgo-api ./helm/chatalgo-api \
  --values ./helm/chatalgo-api/values-dev.yaml \
  --set secrets.mongodb.secretName=chatalgo-mongodb-secret \
  --set secrets.openai.secretName=chatalgo-openai-secret \
  --namespace <namespace> \
  --create-namespace

# 프로덕션 환경
helm install chatalgo-api ./helm/chatalgo-api \
  --values ./helm/chatalgo-api/values-prod.yaml \
  --set secrets.mongodb.secretName=chatalgo-mongodb-secret \
  --set secrets.openai.secretName=chatalgo-openai-secret \
  --namespace <namespace> \
  --create-namespace
```

### 3. Helm Chart 업그레이드

```bash
helm upgrade chatalgo-api ./helm/chatalgo-api \
  --values ./helm/chatalgo-api/values-dev.yaml \
  --set image.tag=<new-version> \
  --namespace <namespace>
```

### 4. Helm Chart 삭제

```bash
helm uninstall chatalgo-api --namespace <namespace>
```

## 설정 가능한 매개변수

| 매개변수 | 설명 | 기본값 |
|---------|------|-------|
| `replicaCount` | 배포할 복제본 수 | `1` |
| `image.repository` | 컨테이너 이미지 저장소 | `chatalgo-api` |
| `image.tag` | 컨테이너 이미지 태그 | `latest` |
| `image.pullPolicy` | 이미지 가져오기 정책 | `IfNotPresent` |
| `service.type` | Kubernetes 서비스 유형 | `ClusterIP` |
| `service.port` | 서비스 포트 | `8080` |
| `ingress.enabled` | Ingress 사용 여부 | `false` |
| `application.mongodb.database` | MongoDB 데이터베이스 이름 | `chatalgo` |
| `secrets.mongodb.secretName` | MongoDB URI가 포함된 시크릿 이름 | 자동 생성 |
| `secrets.openai.secretName` | OpenAI API Key가 포함된 시크릿 이름 | 자동 생성 |
| `resources.limits` | 컨테이너 리소스 제한 | CPU: `1000m`, Memory: `1024Mi` |
| `resources.requests` | 컨테이너 리소스 요청 | CPU: `500m`, Memory: `512Mi` |
| `autoscaling.enabled` | 자동 스케일링 사용 여부 | `false` |
| `autoscaling.minReplicas` | 최소 복제본 수 | `1` |
| `autoscaling.maxReplicas` | 최대 복제본 수 | `3` |
| `livenessProbe.initialDelaySeconds` | Liveness 프로브 초기 지연 시간 | `60` |
| `readinessProbe.initialDelaySeconds` | Readiness 프로브 초기 지연 시간 | `30` |

전체 매개변수 목록은 `values.yaml` 파일을 참조하세요.

## 환경별 설정

### 개발 환경

개발 환경을 위한 설정은 `values-dev.yaml` 파일에 정의되어 있습니다:

```bash
helm install chatalgo-api ./helm/chatalgo-api -f values-dev.yaml
```

### 프로덕션 환경

프로덕션 환경을 위한 설정은 별도로 `values-prod.yaml` 파일을 생성하여 사용할 수 있습니다. 이 파일에서는 더 높은 리소스 제한, 복제본 수, 그리고 자동 스케일링 설정을 구성하는 것이 좋습니다.

## 트러블슈팅

### Pod가 시작되지 않는 경우

```bash
kubectl describe pod -l app.kubernetes.io/name=chatalgo-api
kubectl logs -l app.kubernetes.io/name=chatalgo-api
```

### MongoDB 연결 문제

1. MongoDB URI가 올바르게 설정되었는지 확인:
```bash
kubectl get secret <mongodb-secret-name> -o jsonpath='{.data.mongodb-uri}' | base64 --decode
```

2. 애플리케이션 로그 확인:
```bash
kubectl logs -l app.kubernetes.io/name=chatalgo-api | grep -i mongodb
```

## 모니터링

이 애플리케이션은 Spring Boot Actuator를 통해 상태 정보와 메트릭을 제공합니다:

- 상태 확인: `/actuator/health`
- 메트릭: `/actuator/metrics`
- 정보: `/actuator/info`

Prometheus와 Grafana를 사용하여 애플리케이션을 모니터링하는 것을 권장합니다.

## 라이센스

이 Helm Chart는 MIT 라이센스 하에 배포됩니다.

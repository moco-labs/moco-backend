{{/*
Expand the name of the chart.
*/}}
{{- define "chatalgo-api.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "chatalgo-api.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "chatalgo-api.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "chatalgo-api.labels" -}}
helm.sh/chart: {{ include "chatalgo-api.chart" . }}
{{ include "chatalgo-api.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "chatalgo-api.selectorLabels" -}}
app.kubernetes.io/name: {{ include "chatalgo-api.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "chatalgo-api.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "chatalgo-api.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Define MongoDB secret name
*/}}
{{- define "chatalgo-api.mongodb.secretName" -}}
{{- if .Values.secrets.mongodb.secretName }}
{{- .Values.secrets.mongodb.secretName }}
{{- else }}
{{- include "chatalgo-api.fullname" . }}-mongodb
{{- end }}
{{- end }}

{{/*
Define OpenAI secret name
*/}}
{{- define "chatalgo-api.openai.secretName" -}}
{{- if .Values.secrets.openai.secretName }}
{{- .Values.secrets.openai.secretName }}
{{- else }}
{{- include "chatalgo-api.fullname" . }}-openai
{{- end }}
{{- end }}

{{/*
Define ConfigMap name
*/}}
{{- define "chatalgo-api.configmap.name" -}}
{{- if .Values.configMap.configMapName }}
{{- .Values.configMap.configMapName }}
{{- else }}
{{- include "chatalgo-api.fullname" . }}-config
{{- end }}
{{- end }}

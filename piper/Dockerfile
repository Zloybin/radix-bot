FROM python:3.12-slim

WORKDIR /app

RUN pip install --no-cache-dir "piper-tts[http]"

# Скачиваем немецкую модель во время сборки образа
RUN mkdir -p /data && \
    python -m piper.download_voices \
    de_DE-thorsten-medium \
    --data-dir /data

EXPOSE 5000

ENTRYPOINT ["python", "-m", "piper.http_server"]
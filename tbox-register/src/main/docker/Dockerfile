# Dockerfile for tbox-register
# 1. Build with: docker build -t tbox-register .
# 2. Run with: docker run -p 8761:8761 -d -v /tmp/logs:/opt/logs --name tbox-register tbox-register

FROM openjdk:8-jre-alpine
MAINTAINER ameizi <287357040@qq.com>

ENV VERSION 1.0.0-SNAPSHOT

RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD tbox-register-${VERSION}-github.zip /tbox-register/tbox-register-${VERSION}-github.zip

RUN unzip /tbox-register/tbox-register-${VERSION}-github.zip -d /tbox-register \
    && rm -rf /tbox-register/tbox-register-${VERSION}-github.zip \
    && sed -i '$d' /tbox-register/scripts/startup.sh \
    && echo "tail -f /dev/null" >> /tbox-register/scripts/startup.sh

EXPOSE 8761

CMD ["/tbox-register/scripts/startup.sh"]

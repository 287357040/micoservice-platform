# Dockerfile for tbox-adminservice
# 1. Copy tbox-adminservice-${VERSION}-github.zip to current directory
# 2. Build with: docker build -t tbox-adminservice
# 3. Run with docker run -p 8090:8090 -d -v /tmp/logs:/opt/logs --name tbox-adminservice tbox-adminservice

FROM openjdk:8-jre-alpine
MAINTAINER hcl <287357040@qq.com>

ENV VERSION 1.0.0-SNAPSHOT

# 指定linux-alpine安装软件安装源
RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asiz/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD tbox-adminservice-${VERSION}-github.zip /tbox-adminservice/tbox-adminservice-${VERSION}-github.zip

RUN unzip /tbox-adminservice/tbox-adminservice-${VERSION}-github.zip -d /tbox-adminservice \
    && rm -rf /tbox-adminservice/tbox-adminservice-${VERSION}-github.zip \
    && sed -i '$d' /tbox-adminservice/scripts/startup.sh \
    && echo "tail -f /dev/null" >> /tbox-adminservice/scripts/startup.sh

EXPOSE 8090

CMD ["/tbox-adminservice/scripts/startup.sh"]
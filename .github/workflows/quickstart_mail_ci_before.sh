#!/bin/sh

# Start apache James with the required configuration
docker run -d --rm --name "apache-james" \
  -p 1465:465 \
  -p 1993:993 \
  -p 1025:25 \
  -p 1110:110 \
  -p 1587:587 \
  -p 1143:143 \
  -v ${GITHUB_WORKSPACE}/quickstarts/mail/mail-server-conf/imapserver.xml:/root/conf/imapserver.xml \
  -v ${GITHUB_WORKSPACE}/quickstarts/mail/mail-server-conf/pop3server.xml:/root/conf/pop3server.xml \
  -v ${GITHUB_WORKSPACE}/quickstarts/mail/mail-server-conf/smtpserver.xml:/root/conf/smtpserver.xml \
  apache/james:demo-3.8.0

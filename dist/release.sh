#!/bin/bash
#
# JBoss, Home of Professional Open Source
# Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
# contributors by the @authors tag. See the copyright.txt in the
# distribution for a full listing of individual contributors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


REQUIRED_BASH_VERSION=3.0.0

if [[ $BASH_VERSION < $REQUIRED_BASH_VERSION ]]; then
  echo "You must use Bash version 3 or newer to run this script"
  exit
fi

DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# DEFINE

VERSION_REGEX='([0-9]*)\.([0-9]*)([a-zA-Z0-9\.]*)'

# EAP team email subject
EMAIL_SUBJECT="\${RELEASEVERSION} of JBoss Quickstarts released, please merge with https://github.com/jboss-eap/quickstart, tag and add to EAP maven repo build"
# EAP team email To ?
EMAIL_TO="pgier@redhat.com kpiwko@redhat.com"
EMAIL_FROM="\"JDF Publish Script\" <benevides@redhat.com>"


# SCRIPT

usage()
{
cat << EOF
usage: $0 options

This script performs a release of the Quickstarts 

OPTIONS:
   -s      Snapshot version number to update from
   -n      New snapshot version number to update to, if undefined, defaults to the version number updated from
   -r      Release version number
EOF
}

notify_email()
{
   echo "***** Performing JBoss Quickstarts release notifications"
   echo "*** Notifying WildFly team"
   subject=`eval echo $EMAIL_SUBJECT`
   echo "Email from: " $EMAIL_FROM
   echo "Email to: " $EMAIL_TO
   echo "Subject: " $subject
  # send email using sendmail
   printf "Subject: $subject\nSee \$subject :)\n" | /usr/bin/env sendmail -f "$EMAIL_FROM" "$EMAIL_TO"
}

release()
{
   BRANCH=$(parse_git_branch)
   git checkout -b $RELEASEVERSION
   echo "Regenerating html from markdown"
   $DIR/release-utils.sh -m
   echo "Releasing WildFly Quickstarts version $RELEASEVERSION"
   $DIR/release-utils.sh -u -o $SNAPSHOTVERSION -n $RELEASEVERSION
   echo "Removing unnecessary files"
   git rm --cached -r dist/
   git rm --cached -r template/
   git commit -a -m "Prepare for $RELEASEVERSION release"
   echo "Creating tag for $RELEASEVERSION"
   git tag $RELEASEVERSION 
   mvn clean install -f $DIR/pom.xml
   echo "Your zip file was generated at $DIR/target/wildfly-eap-quickstarts-$RELEASEVERSION-dist.zip"
   $DIR/release-utils.sh -u -o $RELEASEVERSION -n $NEWSNAPSHOTVERSION
   echo "Adding unnecessary files again..."
   git add dist/
   git add template/
   git commit -a -m "Prepare for development of $NEWSNAPSHOTVERSION"
   git checkout $BRANCH
   read -p "Do you want to send release notifcations to $EAP_EMAIL_TO[y/N]?" yn
   case $yn in
       [Yy]* ) notify_email;;
   esac
   echo "Don't forget to push the tag and the branch"
   #echo "   git push --tags upstream refs/heads/$RELEASEVERSION master"
   echo "   git push --tags upstream $BRANCH"
}

parse_git_branch() {
    git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/\1/'
}


SNAPSHOTVERSION="UNDEFINED"
RELEASEVERSION="UNDEFINED"
NEWSNAPSHOTVERSION="UNDEFINED"
MAJOR_VERSION="UNDEFINED"
MINOR_VERSION="UNDEFINED"

while getopts “n:r:s:” OPTION

do
     case $OPTION in
         h)
             usage
             exit
             ;;
         s)
             SNAPSHOTVERSION=$OPTARG
             ;;
         r)
             RELEASEVERSION=$OPTARG
             ;;
         n)
             NEWSNAPSHOTVERSION=$OPTARG
             ;;
         [?])
             usage
             exit
             ;;
     esac
done

if [[ $RELEASEVERSION =~ $VERSION_REGEX ]]; then
   MAJOR_VERSION=${BASH_REMATCH[1]}
   MINOR_VERSION=${BASH_REMATCH[2]}
fi

if [ "$NEWSNAPSHOTVERSION" == "UNDEFINED" ]
then
   NEWSNAPSHOTVERSION=$SNAPSHOTVERSION
fi

if [ "$MAJOR_VERSION" == "UNDEFINED" -o  "$MINOR_VERSION" == "UNDEFINED" ]
then
   echo "\nUnable to extract major and minor versions\n"
   usage
   exit
fi

if [ "$SNAPSHOTVERSION" == "UNDEFINED" -o  "$RELEASEVERSION" == "UNDEFINED" ]
then
   echo "\nMust specify -r and -s\n"
   usage
   exit
fi

release


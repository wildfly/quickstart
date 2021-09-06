#!/bin/sh 
# Usage
# ./changeVersions.sh <XML_ELEMENT_NAME> <OLD_VERSION> <NEW_VERSION> <LIST_FILE_NAMES_TO_CHANGE>
# eg1: ./changeVersions.sh version 24.0.0.Final 25.0.0.Beta1-SNAPSHOT pom.xml
# eg2: ./changeVersions.sh version.server.bom 24.0.0.Final 25.0.0.Beta1-SNAPSHOT pom.xml,README.adoc 

SED='s/\<'$1'\>'$2'\<\/'$1'\>/\<'$1'\>'$3'\<\/'$1'\>/g'
shift 3
for i; do
	find . -type f -name "$i" -print0 | xargs -0 -t sed -i "" -e "$SED"
done
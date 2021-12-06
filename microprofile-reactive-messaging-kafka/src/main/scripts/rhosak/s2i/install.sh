#!/bin/sh

# This script (install.sh) will be executed during build 'server-build-artifacts'
# This script copies the CLI scripts to $JBOSS_HOME/extensions to be processed in the post hook
# The post hook part means that on launching of the application server the postconfigure.sh is executed

set -x
echo "Running microprofile-reactive-messaging-kafka/install.sh"
injected_dir=$1
if [[ "$QS_USE_RHOSAK" == "1" ]] || [[ "$QS_USE_RHOSAK" == "true" ]]; then
  echo "Copying "$1" directory to $JBOSS_HOME/extensions"
  cp -rf ${injected_dir} $JBOSS_HOME/extensions
fi


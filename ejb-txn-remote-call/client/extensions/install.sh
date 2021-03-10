#!/bin/sh

# This script (install.sh) will be executed during build 'client-build-artifacts'
# This script copies the CLI scripts to $JBOSS_HOME/extensions to be processed in the post hook
# The post hook part means that on launching of the application server the postconfigure.sh is executed

set -x
echo "Running ejb-txn-remote-call/client/install.sh"
injected_dir=$1
# copy any needed files into the target build.
cp -rf ${injected_dir} $JBOSS_HOME/extensions

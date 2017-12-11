# Establish global variables to the docs and script dirs
CURRENT_DIR="$( pwd -P)"
BLACK='\033[0;30m'
RED='\033[0;31m'
NO_COLOR="\033[0m"

usage(){
  cat <<EOM
Run this script from the root of your cloned repository: 

  ~/eap-quickstarts $ ./buildReadmes.sh
EOM

}


subdirs=$@
echo "=== Building all the quickstart README.html files ==="
# Recurse through the quickstart directories and build the README HTML files
subdirs=`find -maxdepth 2 -name README.adoc | xargs dirname | sort`

echo $PWD
for subdir in $subdirs
do
  # Navigate to the dirctory and build it
  cd ${subdir##*/}
  echo "Building ${subdir##*/} README.html"
  GUIDE_NAME=${PWD##*/}
  asciidoctor -t -dbook -a toc -o README.html README.adoc
  if [ "$?" = "1" ]; then
    BUILD_ERROR="ERROR: Build of $GUIDE_NAME failed. See the log above for details."
    BUILD_MESSAGE="$BUILD_MESSAGE\n$BUILD_ERROR"
  fi
  # Return to the parent directory
  cd $CURRENT_DIR
done

# Return to where we started as a courtesy.
cd $CURRENT_DIR

# Report any errors
echo ""
if [ "$BUILD_MESSAGE" == "$BUILD_RESULTS" ]; then
  echo "Build was successful!"
  exit;
else
  echo -e "${RED}$BUILD_MESSAGE${NO_COLOR}"
    # This is a build error.
    echo -e "${RED}Please fix all issues before requesting a merge!${NO_COLOR}"
    exit 1;
fi


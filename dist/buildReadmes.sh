## asciidoctor -t -dbook -a toc -o README.html README.adoc
## cd shared-doc/
## asciidoctor -t -dbook -a toc -o development-shortcuts.html development-shortcuts.adoc
## asciidoctor -t -dbook -a toc -o system-requirements.html system-requirements.adoc
## cd ../kitchensink
## asciidoctor -t -dbook -a toc -o README.html README.adoc
## cd ../

usage(){
  cat <<EOM
USAGE: $0 [OPTION]... <guide>

DESCRIPTION: Build all of the guides (default), a single guide, and (optionally)
produce pot/po files for translation.

Run this script from the root of your cloned repo or from the 'scripts'
directory.  Example:
  cd eap-quickstart/
  $0

OPTIONS:
  -h       Print help.

EXAMPLES:
  Build all quickstarts:
   $0

  Build a specific quickstart(s):
    $0 kitchensink
    $0 kitchensink number-guess

EOM
# Now list the valid quickstart names
listquickstarts
}


listquickstarts(){
  echo ""
  echo "  Valid book argument values are:"

  subdirs=`find . -maxdepth 1 -type d ! -iname ".*" ! -iname "dist" ! -iname "shared" ! -iname "src" | sort`
  for subdir in $subdirs
  do
    echo "   ${subdir##*/}"
  done
  echo ""
  # Return to where we started as a courtesy.
}


OPTIND=1
while getopts "ht" c
 do
     case "$c" in
       h)         usage
                  exit 1;;
       \?)        echo "Unknown option: -$OPTARG." >&2
                  usage
                  exit 1;;
     esac
 done
shift $(($OPTIND - 1))


CURRENT_DIR="$( pwd -P)"
SCRIPT_SRC="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd -P )"
QS_BASE="$( dirname $SCRIPT_SRC )"

echo "CURRENT_DIR = $CURRENT_DIR"
echo "SCRIPT_SRC = $SCRIPT_SRC"
echo "QS_BASE = $QS_BASE"

# Set the list of quickstart README files to build to whatever the user passed in (if anyting)
cd $QS_BASE

subdirs=$@
if [ $# -gt 0 ]; then
  # Strip off any ending forward slash
  subdirs=${@%/}
  echo "=== Bulding $subdirs ==="
else
  echo "=== Building all the quickstarts ==="
  # Build the root README first
  asciidoctor -t -dbook -a toc -o README.html README.adoc
  # Recurse through the guide directories and build them.
  subdirs=`find . -maxdepth 1 -type d ! -iname ".*" ! -iname "dist" ! -iname "shared" | sort`
fi
echo $PWD
for subdir in $subdirs
do
  echo "Building ${subdir##*/}"
  # Navigate to the dirctory and build it
  if ! [ -e ${subdir##*/} ]; then
    BUILD_MESSAGE="$BUILD_MESSAGE\nERROR: ${subdir##*/} does not exist."
    # This is a book argument error so we should list the valid arguments.
    LIST_BOOKS="true"
    continue
  fi
  cd ${subdir##*/}
  GUIDE_NAME=${PWD##*/}
  asciidoctor -t -dbook -a toc -o README.html README.adoc
  # Return to the parent directory
  cd $QS_BASE
done


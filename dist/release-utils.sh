#!/bin/bash

REQUIRED_BASH_VERSION=3.0.0

if [[ $BASH_VERSION < $REQUIRED_BASH_VERSION ]]; then
  echo "You must use Bash version 3 or newer to run this script"
  exit
fi


DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# DEFINE

ARCHETYPES=("jboss-javaee6-webapp-archetype" "jboss-javaee6-webapp-ear-archetype")
QUICKSTARTS=("kitchensink" "kitchensink-ear")
VERSIONS_MAVEN_PLUGIN_VERSION=1.3.1

# SCRIPT

HUMAN_READABLE_ARCHETYPES=""
i=0
element_count=${#ARCHETYPES[@]}
index=0
while [ "$index" -lt "$element_count" ]
do
   if [ $index -ne 0 ]
   then
      HUMAN_READABLE_ARCHETYPES="${HUMAN_READABLE_ARCHETYPES}, "
   fi
   HUMAN_READABLE_ARCHETYPES="${HUMAN_READABLE_ARCHETYPES}${ARCHETYPES[index]}"
   ((index++))
done


usage()
{
cat << EOF
usage: $0 options

This script aids in releasing the quickstarts

OPTIONS:
   -u      Updates version numbers in all POMs, used with -o and -n      
   -o      Old version number to update from
   -n      New version number to update to
   -r      Regenerate the various quickstarts based on archetypes ${HUMAN_READABLE_ARCHETYPES}
   -m      Generate html versions of markdown readmes
   -h      Shows this message
EOF
}

update()
{
    cd $DIR/../
    echo "Updating versions from $OLDVERSION TO $NEWVERSION for all Java files under $PWD"
    perl -pi -e "s/${OLDVERSION}/${NEWVERSION}/g" `find . -name \*.java`

    echo "Performing updates to POMs"
    poms=`find . -type f -iname "pom.xml" -maxdepth 2 | sort`
    for pom in $poms
    do
        echo "Updating ${pom}"
        mvn org.codehaus.mojo:versions-maven-plugin:${VERSIONS_MAVEN_PLUGIN_VERSION}:set -DnewVersion=${NEWVERSION} -f ${pom} -q
        mvn org.codehaus.mojo:versions-maven-plugin:${VERSIONS_MAVEN_PLUGIN_VERSION}:commit -f ${pom} -q
    done
}

markdown_to_html()
{
   cd $DIR/../

   # Clear the contents from toc.html file
   rm dist/target/toc.html
   touch dist/target/toc.html

   # Loop through the sorted quickstart directories and process them
   # Exclude the template directory since it's not a quickstart
   subdirs=`find . -maxdepth 1 -type d ! -iname ".*" ! -iname "template" | sort`
   for subdir in $subdirs
   do
      readmes=`find $subdir -iname readme.md`
      for readme in $readmes
      do
         echo "Processing $readme"
         output_filename=${readme//.md/.html}
         output_filename=${output_filename//.MD/.html}
         $DIR/github-flavored-markdown.rb $readme > $output_filename  
      done
   done
   # Now process the root readme
   cd $DIR/../
   readme=README.md
   echo "Processing $readme"
   output_filename=${readme//.md/.html}
   output_filename=${output_filename//.MD/.html}
   $DIR/github-flavored-markdown.rb $readme > $output_filename  

   # Now process the contributing markdown
   cd $DIR/../
   markdown_filename=CONTRIBUTING.md
   echo "Processing $markdown_filename"
   output_filename=${markdown_filename//.md/.html}
   output_filename=${output_filename//.MD/.html}
   $DIR/github-flavored-markdown.rb $markdown_filename > $output_filename  

   # Now process the release procedure markdown
   cd $DIR/../
   markdown_filename=RELEASE_PROCEDURE.md
   echo "Processing $markdown_filename"
   output_filename=${markdown_filename//.md/.html}
   output_filename=${output_filename//.MD/.html}
   $DIR/github-flavored-markdown.rb $markdown_filename > $output_filename  
}

regenerate()
{
   TMPDIR="$DIR/target/regen"
   ROOTDIR="$DIR/../"
   
   rm -rf $TMPDIR

   mkdir -p $TMPDIR

   cd $TMPDIR

   element_count=${#ARCHETYPES[@]}
   index=0
   while [ "$index" -lt "$element_count" ]
   do
      archetype=${ARCHETYPES[index]}
      quickstart=${QUICKSTARTS[index]}
      package=${quickstart//-/_}
      name="JBoss AS Quickstarts: $quickstart"
      echo "**** Regenerating $quickstart from $archetype"
      mvn archetype:generate -DarchetypeGroupId=org.jboss.spec.archetypes -DarchetypeArtifactId=$archetype -DarchetypeVersion=$VERSION -DartifactId=jboss-as-$quickstart -DgroupId=org.jboss.as.quickstarts -Dpackage=org.jboss.as.quickstarts.$package -Dversion=$VERSION -DinteractiveMode=false -Dname="${name}"
      ((index++))
      rm -rf $ROOTDIR/$quickstart
      mv $TMPDIR/jboss-as-$quickstart $ROOTDIR/$quickstart
   done

}

OLDVERSION="1.0.0-SNAPSHOT"
NEWVERSION="1.0.0-SNAPSHOT"
VERSION="1.0.0-SNAPSHOT"
CMD="usage"

while getopts “muo:n:r:” OPTION

do
     case $OPTION in
         u)
             CMD="update"
             ;;
         h)
             usage
             exit
             ;;
         o)
             OLDVERSION=$OPTARG
             ;;
         n)
             NEWVERSION=$OPTARG
             ;;
         r) 
             CMD="regenerate"
             VERSION=$OPTARG
             ;;
         m)
             CMD="markdown_to_html"
             ;;
         [?])
             usage
             exit
             ;;
     esac
done

$CMD


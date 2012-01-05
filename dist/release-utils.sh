#!/bin/sh

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ] ; do SOURCE="$(readlink "$SOURCE")"; done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

# DEFINE

ARCHETYPES=("jboss-javaee6-webapp-archetype" "jboss-javaee6-webapp-ear-archetype" "jboss-html5-mobile-archetype")
QUICKSTARTS=("kitchensink" "kitchensink-ear" "html5-mobile")

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
echo "Updating versions from $OLDVERSION TO $NEWVERSION for all Java and XML files under $PWD"
perl -pi -e "s/${OLDVERSION}/${NEWVERSION}/g" `find . -name \*.xml -or -name \*.java`
}

markdown_to_html()
{
   cd $DIR/../
   readmes=`find . -iname readme.md -or -iname contributing.md`
   echo $readmes
   for readme in $readmes
   do
      output_filename=${readme//.md/.html}
      output_filename=${output_filename//.MD/.html}
      markdown $readme -f $output_filename  
   done
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
      echo "\n**** Regenerating $quickstart from $archetype\n"
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


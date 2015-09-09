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


# Require BASH 3 or newer

REQUIRED_BASH_VERSION=3.0.0

if [[ $BASH_VERSION < $REQUIRED_BASH_VERSION ]]; then
  echo "You must use Bash version 3 or newer to run this script"
  exit
fi

# Canonicalise the source dir, allow this script to be called anywhere
DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

# DEFINE

TARGET=target/guides
MASTER=guide.asciidoc

OUTPUT_FORMATS=("xml" "epub" "pdf")
OUTPUT_CMDS=("asciidoc -b docbook -o \${output_filename} \$MASTER" "a2x -L -f epub -D \$dir \$MASTER" "a2x -L --dblatex-opts \"-P latex.output.revhistory=0\" -D \$dir \$MASTER")

echo "** Building tutorial"

echo "**** Cleaning $TARGET"
rm -rf $TARGET
mkdir -p $TARGET

output_format=html
dir=$TARGET/$output_format
mkdir -p $dir
echo "**** Copying shared resources to $dir"
cp -r gfx $dir

for file in *.asciidoc 
do
   output_filename=$dir/${file//.asciidoc/.$output_format}
   echo "**** Processing $file > ${output_filename}"
   asciidoc -a pygments -b html5 -a toc2 -a icons -a iconsdir=gfx/icons -o ${output_filename} $file
done

for ((i=0; i < ${#OUTPUT_FORMATS[@]}; i++))
do
   output_format=${OUTPUT_FORMATS[i]}
   dir=$TARGET/$output_format
   output_filename=$dir/${file//.asciidoc/.$output_format}
   mkdir -p $dir
   echo "**** Copying shared resources to $dir"
   cp -r gfx $dir
   echo "**** Processing $file > ${output_filename}"
   eval ${OUTPUT_CMDS[i]}
done


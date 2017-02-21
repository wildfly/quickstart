#!/bin/sh

# "Enter passphrase for ..." is a prefix required by kwallet for example
if [ "$1" = "Enter passphrase for askpass quickstart" ]; then
    echo vault22
else
    echo $1
fi

#!/bin/sh

## program execution information
package="se.kth.maandree.unisay"
main="Unisay"


## java executer if default is for Java 7
[[ $(echo `java -version 2>&1 | cut -d . -f 2` | cut -d ' ' -f 1) = '7' ]] &&
    function javaSeven()
    {   java "$@"
    }

## java executer if default is not for Java 7
[[ $(echo `java -version 2>&1 | cut -d . -f 2` | cut -d ' ' -f 1) = '7' ]] ||
    function javaSeven()
    {   java7 "$@"
    }


## libraries
jars=''
if [ -d lib ]; then
    jars=`echo $(find lib | grep '\.jar$') | sed -e 's/lib\//:lib\//g' -e 's/ //g'`
fi

## completion
if [[ $1 = "--completion--" ]]; then
    . share/bash-completion/completions/unisay
    complete -o default -F _unisay run

## run
else
    fortune | javaSeven -ea -cp bin$jars "$package"."$main" "$@"

fi

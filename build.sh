#!/bin/sh

# This script is put in Public Domain
# Year: 2012
# Author: Mattias Andrée (maandree@kth.se)


paramMake=0
for opt in "$@"; do
    if [[ $opt = '-make' ]]; then
	paramMake=1
    fi
done


## completion
if [[ $paramMake = 0 ]]; then
    . run.sh --completion--
fi


## create directory for Java binaries
mkdir -p bin


## in with resources to bin/
if [ -d res ]; then
    cp -r res bin
fi


## java compiler if default is for Java 7
[[ $(javac -version 2>&1 | cut -d . -f 2) = '7' ]] &&
    function javacSeven()
    {   javac "$@"
    } &&
    function jarSeven()
    {   jar "$@"
    }

## java compiler if default is not for Java 7
[[ $(javac -version 2>&1 | cut -d . -f 2) = '7' ]] ||
    function javacSeven()
    {   javac7 "$@"
    } &&
    function jarSeven()
    {   jar7 "$@"
    }


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


## warnings
warns="-Xlint:all"

## standard parameters
params="-source 7 -target 7 -s src -d bin"


## libraries
jars=''
if [ -d lib ]; then
    jars=`echo $(find lib | grep '\.jar$') | sed -e 's/lib\//:lib\//g' -e 's/ //g'`
fi


## parse options
function _javac()
{   javac "$@"
}
paramEcho=0
paramEcj=0
for opt in "$@"; do
    if [[ $opt = '-ecj' ]]; then
	paramEcj=1
	if [ -f ./ecj.jar ]; then
	    function _ecj()
	    {   javaSeven -jar ecj.jar "$@"
	    }
	else
	    function _ecj()
	    {   ecj "$@"
	    }
	fi
	if [ -d /opt/java7/jre/lib ]; then
	    function javacSeven()
	    {   _ecj -bootclasspath `echo $(find /opt/java7/jre/lib | grep .jar$) | sed -e 's/\/opt\/java7\/jre\/lib\//:\/opt\/java7\/jre\/lib\//g' -e 's/ //g' | dd skip=1 bs=1 2>/dev/null` "$@"
	    }
	else
	    function javacSeven()
	    {   _ecj "$@"
	    }
	fi
	function _javac()
	{   javacSeven -source 6 -target 6 "$@"
	}
	errs="-err:conditionAssign,noEffectAssign,enumIdentifier,hashCode"
	warns=$errs" -warn:allDeadCode,allDeprecation,allOver-ann,all-static-method,assertIdentifier,boxing,charConcat,compareIdentical,constructorName,deadCode,dep-ann,deprecation,"
	warns+="discouraged,emptyBlock,enumSwitch,fallthrough,fieldHiding,finalBound,finally,forbidden,includeAssertNull,indirectStatic,intfAnnotation,intfNonInherited,intfRedundant,"
	warns+="localHiding,maskedCatchBlock,null,nullDereference,over-ann,paramAssign,pkgDefaultMethod,raw,semicolon,serial,static-method,static-access,staticReceiver,suppress,"
	warns+="syncOverride,syntheticAccess,typeHiding,unchecked,unnecessaryElse,unqualifiedField,unusedAllocation,unusedArgument,unusedImport,unusedLabel,unusedLocal,unusedPrivate,"
	warns+="unusedThrown,uselessTypeCheck,varargsCast,warningToken"
	#unused: enumSwitchPedantic,nls,specialParamHiding,super,switchDefault,unavoidableGenericProblems,nullAnnot,tasks
	#sorry: javadoc,resource,unusedTypeArgs
    elif [[ $opt = '-echo' ]]; then
	paramEcho=1
	function _javac()
	{   echo "$@"
	}
    elif [[ $opt = '-q' ]]; then
	warns=''
    fi
done


## colouriser
function colourise()
{
    if [[ $paramMake = 1 ]]; then
	cat
    elif [[ $paramEcho = 1 ]]; then
        cat
    elif [[ $paramEcj = 1 ]]; then
	if [[ -f "dev/colourpipe.ecj.jar" ]]; then
            javaSeven -jar dev/colourpipe.ecj.jar
	fi
    elif [[ -f "dev/colourpipe.javac.jar" ]]; then
        javaSeven -jar dev/colourpipe.javac.jar
    else
	cat
    fi
}


 ## exception generation
if [ -f 'src/se/kth/maandree/javagen/ExceptionGenerator.java' ]; then
    ## compile exception generator
    ( javacSeven $warns -cp . $params 'src/se/kth/maandree/javagen/ExceptionGenerator.java'  2>&1
    ) | colourise &&
    
    ## generate exceptions code
    javaSeven -ea -cp bin$jars "se.kth.maandree.javagen.ExceptionGenerator" -o bin -- $(find src | grep '\.exceptions$')  2>&1  &&
    echo -e '\n\n\n'  &&
    
    ## generate exceptions binaries
    ( javacSeven $warns -cp bin$jars -source 7 -target 7 $(find bin | grep '\.java$')  2>&1
    ) | colourise
fi

## compile ponypipe
( _javac $warns -cp .:bin$jars -s src -d bin $(find src | grep '\.java$')  2>&1
) | colourise

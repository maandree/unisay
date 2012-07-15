#!/bin/sh

# This script is put in Public Domain
# Year: 2012
# Author: Mattias Andrée (maandree@kth.se)


for pony in $(ls --color=no share/unisay/pony/); do
    echo "building ttypony: $pony"
    if [[ `readlink "share/unisay/pony/$pony"` = '' ]]; then
	unisay2ttyunisay < "share/unisay/pony/$pony" | tty2colourfultty -c 1 > "share/unisay/ttypony/$pony"
    elif [[ ! -f "share/unisay/ttypony/$pony" ]]; then
	ln -s `readlink "share/unisay/pony/$pony"` "share/unisay/ttypony/$pony"
    fi
done


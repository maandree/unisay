#!/bin/sh

for pony in $(ls --color=no share/unisay/pony/); do
    echo "building ttypony: $pony"
    unisay2ttyunisay < "share/unisay/pony/$pony" | tty2colourfultty -c 1 > "share/unisay/ttypony/$pony"
done


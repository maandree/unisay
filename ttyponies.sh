#!/bin/sh

for $pony in $(ls share/unisay/pony/); do
    unisay2ttyunisay < "share/unisay/pony/$pony" | unzebra | tty2colourfultty -c 1 > "share/unisay/ttypony/$pony"
done


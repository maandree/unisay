#!/bin/sh

for ponies in $(cat share-src/unisay/ponyquotes/ponies); do
    for pony in $(echo $ponies | sed -e 's/+/ /g'); do

	for file in $(ls "share-src/unisay/ponyquotes/" | grep "$pony\\.*"); do
	    
	    if [[ -f "share-src/unisay/ponyquotes/$file" ]]; then
		cp "share-src/unisay/ponyquotes/"$file "share/unisay/ponyquotes/"$ponies'.'$(echo $file | cut -d '.' -f 2)
	    fi
	    
	done

    done
done


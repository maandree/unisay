cowsay+ponysay rewritten in Java, with added features such as
pony quotes and full Unicode(!) support.


![screenshot](https://github.com/maandree/unisay/raw/master/screenshot.png)


Note that unisay does not use same options as ponysay or cowsay,
start with --help for more information, with or without options.

Install
-------

To install manually copy the directories `bin` and `share` to either `/usr` or `~/.local`. Also, to either `/usr/bin` or `~/.local/bin`, add `unisay.jar`, which is built by running:
    
    cd src
    javac -cp . se/kth/maandree/unisay/*.java
    jar -cfm ../bin/unisay.jar META-INF/MANIFEST.MF se/kth/maandree/unisay/*.class
    rm se/kth/maandree/unisay/*.class
    cd ..

In the same directory as `unisay.jar` add a file `unisay` containing to code:

    java -jar $0.jar $@

### Displaying fortune cookies

This requires that you have the `fortune` command installed.
To install the command on Arch Linux run the installation command:

    pacman -S fortune-mod

There are several additions to fortune available in AUR.

Fortune cookies are displayed by running:

    fortune | unisay

If you want ponified fortune cookies use `pinkiepie` (`pinkie`) instead
of `fortune`. pinkie-pit is available in AUR under then name `pinkie-pie-git`.

### Run on terminal startup

To run, for example, `unisay -q -p unicode -P` when the terminal starts,
but `fortune | unisay -C` when using Linux VT; add the follow to your `~/.bashrc`:

    if [ "$TERM" = "linux" ]; then
        unisay -p linux-vt -P
    else
        unisay -p unicode -P
    fi

### Ponies in TTY (Linux VT)

If you have a custom colour palette edit your `~/.bashrc` and add

    if [ "$TERM" = "linux" ]; then
        function unisay
        {
            exec unisay $@
            #RESET PALETTE HERE
        }
    fi


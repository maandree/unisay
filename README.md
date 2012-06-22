cowsay+ponysay rewritten in Java, with added features such as
pony quotes and full Unicode(!) support.


![screenshot](https://github.com/maandree/unisay/raw/master/screenshot.png)


Note that unisay does not use same options as ponysay or cowsay,
start with --help for more information, with or without options.

Install
-------

To install unisay, just run the command

    sudo make install

### Displaying fortune cookies

This requires that you have the `fortune` command installed.
To install the command on Arch Linux run the installation command:

    pacman -S fortune-mod

There are several additions to fortune available in AUR.

Fortune cookies are displayed by running:

    fortune | unisay

If you want ponified fortune cookies use `pinkiepie` (`pinkie`) instead
of `fortune`. pinkie-pit is available in AUR under the name `pinkie-pie-git`.

### Run on terminal startup

To run, for example, `unisay -q -p unicode -P` when the terminal starts,
but `unisay -q -p linux-vt -P` when using Linux VT; add the follow to your `~/.bashrc`:

    if [ "$TERM" = "linux" ]; then
        unisay -q -p linux-vt -P
    else
        unisay -q -p unicode -P
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


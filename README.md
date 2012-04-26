cowsay+ponysay rewritten in Java, with added features such as
pony quotes and full Unicode(!) support.


![screenshot](https://github.com/maandree/unisay/raw/master/screenshot.png)


All ponies are cannibalised from [erkin/ponysay](/erkin/ponysay)
and [svenstaro/qponies](/svenstaro/qponies), and all cows are
cannibalised from cowsay.

Note that unisay does not use same options as ponysay or cowsay,
start with --help for more information, with or without options.

Install
-------

To install manually copy the directories `cow`, `format`, `pony` and `ponyquotes` to either `/usr` or `~/.local`. Also, to either `/usr/bin` or `~/.local/bin`, add `unisay.jar`, which is built by running:

    javac -cp . se/kth/maandree/unisay/*.se
    jar -cfm unisay.jar META-INF/MANIFEST.MF se/kth/maandree/unisay/*.class

In the same directory as `unisay.jar` add a file `unisay` containing to code:

    java -jar $0.jar $@

### Displaying fortune cookies

This requires that you have the `fortune` command installed.
To install the command on Arch Linux run the installation command:

    pacman -S fortune-mod

There are several additions to fortune available in AUR.

Fortune cookies are displayed by running:

    fortune | unisay

### Run on terminal startup

To run, for example, `unisay -q -p unicode -P` when the terminal starts,
but `fortune | unisay -C` when using Linux VT; add the follow to your `~/.bashrc`:

    if [ "$TERM" = "linux" ]; then
        fortune | unisay -C
    else
        unisay -q -p unicode -P
    fi


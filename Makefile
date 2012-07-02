all: unisay

unisay:
	javac -cp . -s src -d . $$(find ./src | grep '\.java$$')
	jar -cfm unisay.jar META-INF/MANIFEST.MF $$(find ./se | grep '\.class$$')
	rm -r se

install: all
	install -d "${DESTDIR}/usr/bin"
	install -d "${DESTDIR}/usr/share/bash-completion/completions"
	install -d "${DESTDIR}/usr/share/licenses/unisay"
	install -d "${DESTDIR}/usr/share/unisay/cow"
	install -d "${DESTDIR}/usr/share/unisay/format"
	install -d "${DESTDIR}/usr/share/unisay/pony"
	install -d "${DESTDIR}/usr/share/unisay/ponyquotes"
	install -d "${DESTDIR}/usr/share/unisay/ttypony"
	install -m 755 unisay{,.jar} "${DESTDIR}/usr/bin"
	install -m 644 share/bash-completion/completions/* "${DESTDIR}/usr/share/bash-completion/completions"
	install -m 644 share/licenses/unisay/* "${DESTDIR}/usr/share/licenses/unisay"
	install -m 644 share/unisay/modes "${DESTDIR}/usr/share/unisay/modes"
	install -m 644 share/unisay/cow/* "${DESTDIR}/usr/share/unisay/cow"
	install -m 644 share/unisay/format/* "${DESTDIR}/usr/share/unisay/format"
	install -m 644 share/unisay/pony/* "${DESTDIR}/usr/share/unisay/pony"
	install -m 644 share/unisay/ponyquotes/* "${DESTDIR}/usr/share/unisay/ponyquotes"
	install -m 644 share/unisay/ttypony/* "${DESTDIR}/usr/share/unisay/ttypony"

uninstall:
	unlink "${DESTDIR}/usr/bin/unisay"
	unlink "${DESTDIR}/usr/bin/unisay.jar"
	unlink "${DESTDIR}/usr/share/bash-completion/completions/unisay"
	rm -rf "${DESTDIR}/usr/share/liceses/unisay"
	rm -rf "${DESTDIR}/usr/share/unisay"

src_path=src/WhataPOS
pkg_path=production

all:
	javac -cp $(src_path) -d production $(src_path)/*.java --module-path /usr/lib/jvm/java-11-openjfx/lib/ --add-modules javafx.controls,javafx.fxml -Xlint:none
	cp $(src_path)/*.fxml $(pkg_path)/WhataPOS

run:
	java -cp "$(pkg_path)" --module-path /usr/lib/jvm/java-11-openjfx/lib/ --add-modules javafx.controls,javafx.fxml WhataPOS.Main

src_path=src/WhataPOS
pkg_path=production
lib_path=/usr/lib/jvm/java-11-openjfx/lib/

all:
	javac -cp "$(src_path):gson-2.8.6.jar" -d production $(src_path)/*.java --module-path $(lib_path) --add-modules javafx.controls,javafx.fxml -Xlint:none
	cp $(src_path)/*.fxml $(pkg_path)/WhataPOS
	cp -r $(src_path)/assets $(pkg_path)/WhataPOS

run:
	# if on windows change -cp delimiter to ;
	# if on linux change -cp delimiter to :
	java -cp "$(pkg_path):postgresql-42.2.19.jar:gson-2.8.6.jar" --module-path $(lib_path) --add-modules javafx.controls,javafx.fxml WhataPOS.Main

clean:
	rm -rf  production/*

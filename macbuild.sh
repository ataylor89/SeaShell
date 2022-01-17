mvn clean install
mvn assembly:single
jpackage --input target/ \
  --name SeaShell \
  --main-jar SeaShell-jar-with-dependencies.jar \
  --main-class seashell.SeaShell \
  --type dmg \
  --icon "icons.icns" \
  --app-version "1.0.0" \
  --vendor "Andrew's software" \
  --copyright "Copyright 2021" \
  --mac-package-name "SeaShell" \
  --verbose \
  --java-options '--enable-preview'

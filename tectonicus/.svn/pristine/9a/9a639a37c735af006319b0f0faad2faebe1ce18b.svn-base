rm -rf release/classes
java -jar utils/proguard.jar @utils/3DzzDV2_linux.pro

cp -rf release/classes release/classes_tmp

cd release/classes_tmp

jar cf ../extension.jar net/dzzd/extension/loader
jarsigner -keypass 123456 ../extension.jar 3dzzd
rm -rf net/dzzd/extension/loader


jar cf ../DzzDExtensionJOGL.jar net/dzzd/extension/jogl
rm -rf net/dzzd/extension/jogl


rm -rf net/dzzd/extension
jar -cf ../dzzd.jar net



cd ../..
rm -rf release/classes_tmp

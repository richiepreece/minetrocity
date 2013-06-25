cp -rf debug/classes debug/classes_tmp

cd debug/classes_tmp

jar cf ../extension.jar net/dzzd/extension/loader
jarsigner -keypass 123456 ../extension.jar 3dzzd
rm -rf net/dzzd/extension/loader


jar cf ../DzzDExtensionJOGL.jar net/dzzd/extension/jogl
rm -rf net/dzzd/extension/jogl


rm -rf net/dzzd/extension
jar -cf ../dzzd.jar net



cd ../..
rm -rf debug/classes_tmp

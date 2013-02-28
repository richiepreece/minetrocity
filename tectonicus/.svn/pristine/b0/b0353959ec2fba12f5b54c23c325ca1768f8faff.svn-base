
mkdir debug\classes_tmp
xcopy debug\classes debug\classes_tmp /s /y

cd debug/classes_tmp

jar cf ../extension.jar net\dzzd\extension\loader
jarsigner -keypass 123456 ../extension.jar 3dzzd
rd /s /q net\dzzd\extension\loader


jar cf ../DzzDExtensionJOGL.jar net\dzzd\extension\jogl
rd /s /q net\dzzd\extension\jogl


rd /s /q net\dzzd\extension
jar -cf ../dzzd.jar net



cd ../..
rd /s /q debug\classes_tmp

pause
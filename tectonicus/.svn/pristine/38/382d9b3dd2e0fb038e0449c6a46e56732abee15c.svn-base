
rd /s /q release\classes
java -jar utils/proguard.jar @utils/3DzzDV2.pro

mkdir release\classes_tmp
xcopy release\classes release\classes_tmp /s /y

cd release/classes_tmp

jar cf ../extension.jar net\dzzd\extension\loader
jarsigner -keypass 123456 ../extension.jar 3dzzd
rd /s /q net\dzzd\extension\loader


jar cf ../DzzDExtensionJOGL.jar net\dzzd\extension\jogl
rd /s /q net\dzzd\extension\jogl


rd /s /q net\dzzd\extension
jar -cf ../dzzd.jar net



cd ../..
rd /s /q release\classes_tmp

pause
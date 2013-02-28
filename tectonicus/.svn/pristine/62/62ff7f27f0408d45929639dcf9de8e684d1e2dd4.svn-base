keytool -genkey -dname "cn=3DzzD, ou=WebPlugin, o=DzzD,c=FR" -alias 3dzzd -keypass 123456 -storepass 123456 -validity 999
keytool -selfcert  -storepass 123456 -alias 3dzzd  -validity 999
keytool -export  -storepass 123456 -alias 3dzzd -rfc -file 3dzzd.cer
keytool -import -alias 3dzzd -file 3dzzd.cer -keystore dzzdstore

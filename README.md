# KoneksiPG4
connect pgadmin4 w/ maven
 create folder <buka dengan vscode>
 ctrl+shift+p - java create java project
 masukan databaseconnection.java dibagian ..\demo\src\main\java\com\example
 kemudian masukan file setting(setting.txt) ke ..\demo\src\main\resources
 setelah menyesuaikan databaseconnection.java dan setting file
 masukan depedencies pada file pom.xml 

 pada bagian setelah properties bagian bawah sisipkan :
 <dependencies>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.4</version>
        </dependency>
    </dependencies>

setelah ditambahkan lalukan konversi ke .jar
ctrl+shift+p java export jar
 

// Kompilieren: javac -d bin src/main/swe4/Hello.java -> class-Files kommen in bin Ordner
// Ausführen: java --class-path bin swe4.Hello
// JAR aus bin erzeugen: jar --create --file=jar/Hello.jar -C bin .
// Ausführen mit JAR-Inhalt in Classpath: java --class-path .\jar\hello.jar swe4.Hello

// JAR erzeugen, das manifest.mf beinhaltet, in der die Main-Class steht
// -> Programm ist einfacher ausführbar
// jar --create --file=jar/Hello.jar --main-class swe4.Hello -C bin .
// java -jar .\jar\hello.jar

// --- Verwendung von ant ---

// Angepasst in build.xml:
// 1. <project name="hello" ...
// 2. <property name="main.class" value="swe4.Hello"/>

// ant clean -> loescht bin Verzeichniss
// ant compile
// ant run
// ant create-jar
// ant run-jar

package swe4; // Package muss mit Ordnerstruktur uebereinstimmen

// Ein Java-File darf nur eine oeffentliche Klasse beinhalten
// diese muss mit dem Filenamen uebereinstimmen
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello Java");
    }
}
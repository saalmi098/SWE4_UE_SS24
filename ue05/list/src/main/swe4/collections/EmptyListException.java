package swe4.collections;

// Checked Exception
/*public class EmptyListException extends Exception {
    public EmptyListException(String msg) {
        super(msg);
    }
}*/

// Runtime Exception
// Unterschied: Exception muss nicht mehr behandelt werden im Gegensatz zur Checked Exception
// (d.h. man kann in first(), last() das throws in der Signatur weglassen, und in main ist kein try/cath mehr notwendig)
// -> dann bricht das Programm ab mit einem Hinweis, dass diese Exception aufgetreten ist
public class EmptyListException extends RuntimeException {
    public EmptyListException(String msg) {
        super(msg);
    }
}
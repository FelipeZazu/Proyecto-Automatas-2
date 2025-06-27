// Listax.java
package ArbolSintactico;

public class Listax {
    public Statx statement;
    public Listax next;

    public Listax(Statx statement, Listax next) {
        this.statement = statement;
        this.next = next;
    }
}

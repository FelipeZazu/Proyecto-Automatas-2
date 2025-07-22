package ArbolSintactico;

public class Beginx extends Statx {
    public Statx statement;
    public Listax list;

    public Beginx(Statx statement, Listax list) {
        this.statement = statement;
        this.list = list;
    }

    @Override
    public String getBytecode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBytecode'");
    }
}

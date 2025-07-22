
package ArbolSintactico;

public class whilex extends Statx{
    private int id;
    private Expx s1;
    private Statx s2;
    
    public whilex(int id,Expx st1, Statx st2) {
        this.id = id;
        s1 = st1;
        s2 = st2;
    }
    
    public Object[] getVariables() {
        Object obj[] = new Object[2];
        obj[0] = s1;
        obj[1] = s2;
        return obj;
    }

    @Override
    public String getBytecode() {
        // la expresion siempre sera ==, espero...
        String code="\n";
        code += "WHILE_START_"+id+"\n";             // Inicio del While
        code += s1.getBytecode();              // Bytecode de la expresion
        code += " WHILE_END_"+id+"\n";          // Salto al ELSE si CMP_EQ dio 0
        code += s2.getBytecode()+"\n";              // Codigo
        code += "goto WHILE_START_"+id+"\n";         // Regreso al Inicio
        code += "WHILE_END_"+id+":\n";              // Fin
        return code;
    }
}

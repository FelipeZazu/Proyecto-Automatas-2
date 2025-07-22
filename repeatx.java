
package ArbolSintactico;

public class repeatx extends Statx{
    private int id;
    private Expx s1;
    private Statx s2;
    
    public repeatx(int id,Expx st1, Statx st2) {
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
        code += "REPEAT_START_"+id+":\n";             // Inicio del repeat
        code += s2.getBytecode()+"\n";              // Codigo
    
        code += s1.getBytecode();              // Bytecode de la expresion
        code += " REPEAT_END_"+id+"\n";         // Regreso al Inicio
        code += "goto REPEAT_START_"+id+"\n";         // Regreso al Inicio
        code += "REPEAT_END_"+id+":\n";         // Regreso al Inicio

        
        return code;
    }
}

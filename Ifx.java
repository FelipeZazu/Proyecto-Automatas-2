
package ArbolSintactico;

public class Ifx extends Statx{
    private int id;
    private Expx s1;
    private Statx s2;
    private Statx s3;
    
    public Ifx(int id,Expx st1, Statx st2, Statx st3) {
        this.id = id;
        s1 = st1;
        s2 = st2;
        s3 = st3;  
    }
    
    public Object[] getVariables() {
        Object obj[] = new Object[3];
        obj[0] = s1;
        obj[1] = s2;
        obj[2] = s3;
        return obj;
    }
    
    @Override
    public String getBytecode() {
        // la expresion siempre sera ==, espero...
        String code="\n";
        code += s1.getBytecode();              // Bytecode de la expresion
        code += " ELSE_"+id+"\n";               // Salto al ELSE si CMP_EQ dio 0
        code += s2.getBytecode()+"\n";              // Bytecode del si
        code += "goto IF_END_"+id+"\n";              // Salto al Fin del IF
        code += "ELSE_"+id+":\n";                   // Etiqueta ELSE
        code += s3.getBytecode()+"\n";              // ByteCode del ELSE
        code += "IF_END_"+id+":\n";                 // If END
    
        return code;
    }
}

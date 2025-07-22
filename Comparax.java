package ArbolSintactico;

public class Comparax extends Expx{
    private Expx s1;
    private Expx s2;
    
    public Comparax(Expx st1, Expx st2) {
        s1 = st1;
        s2 = st2;
    }

    @Override
    public String getBytecode() {
        String temp="";
        String code="";
        code += s1.getBytecode()+"\n";
        if( s1 instanceof Idx && s2 instanceof Idx){
            temp = ((Idx) s1).cast( (Idx) s2 ); 
            if(!temp.isBlank())
                code += temp+"\n";
        }
        code += s2.getBytecode()+"\n";
        if( s1 instanceof Idx && s2 instanceof Idx){
            temp = ((Idx) s2).cast( (Idx) s1 ); 
            if(!temp.isBlank())
                code += temp+"\n";

             code += ((Idx) s2 ).if_bytecode();
        }
        return code;
    }

}

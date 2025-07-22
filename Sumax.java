
package ArbolSintactico;

public class Sumax extends Expx{
    private Idx s1;
    private Idx s2;
    
    public Sumax(Idx st1, Idx st2){ 
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
        }
        code += s1.getAritmeticByteCode(0);
        return code;
    }

}

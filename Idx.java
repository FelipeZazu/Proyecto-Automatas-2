
package ArbolSintactico;

public class Idx extends Expx{
    private String s1;
    private int id;
    private int type; // 0: INT, 1: FLOAT, 2: DOUBLE, 3: LONG
    private static String load_store[][] = new String[][]{ {"iload","istore"},{"fload","fstore"},{"dload","dstore"},{"lload","lstore"} };
    private static String Aritmetic[][] = {
        {"iadd","isub","imul","idiv"},
        {"fadd","fsub","fmul","fdiv"},
        {"dadd","dsub","dmul","ddiv"},
        {"ladd","lsub","lmul","ldiv"},
    };
    private static String cast_method[][] = {
        // INT
        {"","i2f","i2d","i2l"},
        // FLOAT
        {"f2i","","f2d","f2l"},
        // DOUBLE
        {"d2i","d2f","","d2l"},
        // LONG
        {"l2i","l2f","l2d",""}
    };
    private static String if_code[] = {
        "if_icmpne","fcmpl\nifne","dcmpl\nifne","lcmp\nifne"
    };

    public Idx(String st1,int idt,int type) {
        s1 = st1;
        id = idt;
        this.type = type;
    }
    
    public String getIdx() {
        return s1;
    }

    public String cast(Idx id){
        int pre_type = type;
        if(type <= id.type)
            type = id.type;
        else
            return "";
        return cast_method[pre_type][id.type];
    }

    public String if_bytecode(){
        return if_code[type];
    }

    @Override
    public String getBytecode() {
        return load_store[type][0] +"_"+ id;
    }
    public String getStoreBytecode() {
        return load_store[type][1] +"_"+ id;
    }
    public String getAritmeticByteCode(int operation){ // 0: sum, 1: res, 2: mul, 3: div
        return Aritmetic[type][operation];
    }
}

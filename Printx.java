
package ArbolSintactico;

public class Printx extends Statx{
        Expx s1;
        
        public Printx(Expx st1) {
            s1 = st1;
        }

        @Override
        public String getBytecode() {
            String code="getstatic\n";
            code += s1.getBytecode()+"\n";
            code += "invokevirtual";
            return code;
        }
}

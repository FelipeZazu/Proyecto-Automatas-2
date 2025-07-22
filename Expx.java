
package ArbolSintactico;

public abstract class Expx {

    public abstract String getBytecode();

}


/*
     1. CARGA (load) Y ALMACENAMIENTO (store)
        ----------------------------------------
        iload <n>       - Carga int desde variable local n
        lload <n>       - Carga long
        fload <n>       - Carga float
        dload <n>       - Carga double
        aload <n>       - Carga referencia

        istore <n>      - Almacena int
        lstore <n>      - Almacena long
        fstore <n>      - Almacena float
        dstore <n>      - Almacena double
        astore <n>      - Almacena referencia

    2. OPERACIONES ARITMÉTICAS
        ---------------------------
            | int   | long  | float | double
        ------+--------+-------+-------+--------
        add   | iadd  | ladd  | fadd  | dadd
        sub   | isub  | lsub  | fsub  | dsub
        mul   | imul  | lmul  | fmul  | dmul
        div   | idiv  | ldiv  | fdiv  | ddiv
        rem   | irem  | lrem  | frem  | drem
        neg   | ineg  | lneg  | fneg  | dneg
 */
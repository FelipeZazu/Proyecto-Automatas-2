//package Compiler;

import javax.swing.JOptionPane;
import ArbolSintactico.*;
import java.util.Vector;
import org.apache.commons.lang3.ArrayUtils;

public class Parser {
    //Declaración de variables----------------
    Programax p = null;
    String[] tipo = null;
    String[] variable;
    String byteString;
    private Vector tablaSimbolos = new Vector();
    private final Scanner s;
    final int ifx=1, thenx=2, elsex=3, beginx=4, endx=5, printx=6, semi=7,
            sum=8, igual=9, igualdad=10, intx=11, floatx=12, id=13, doublex=14, longx=15,divix=16,multix=17,restax=18;
    private int tknCode, tokenEsperado;
    private String token, tokenActual, log;
    
    //Sección de bytecode
    private int cntBC = 0; // Contador de lineas para el código bytecode
    private String bc; // String temporal de bytecode
    private int jmp1, jmp2, jmp3;
    private int aux1, aux2, aux3;
    private String pilaBC[] = new String[100];
    private String memoriaBC[] = new String[10];
    private String pilaIns[] = new String [50];
    private int retornos[]= new int[10];
    private int cntIns = 0;
    //---------------------------------------------
  
/*     public static void main(String[] args){
        //var1 int ; var2 int; if var1 == var2 then print var1 + var2 else begin	if var1 + var2 then var1 := var2 + var1	else var2 := var1 + var2 end
        new Parser("var1 int ; var2 int ; var1 := var2 + var1 ; print var1 + var2 ;");
    } */
    
    public Parser(String codigo) {  
        s = new Scanner(codigo);
        token = s.getToken(true);
        tknCode = stringToCode(token);
        p = P();
    }
    
    //INICIO DE ANÁLISIS SINTÁCTICO
    public void advance() {
        token = s.getToken(true);
        tokenActual = s.getToken(false);
        tknCode = stringToCode(token);
    }
    
    public void eat(int t) {
        System.out.println(t);
        tokenEsperado = t;
        if(tknCode == t) {
            setLog("Token: " + token + "\n" + "Tipo:  "+ s.getTipoToken());
            advance();
        }
        else{
            error(token, "token tipo:"+t);
        }
    }
    
    public Programax P() {
        Declarax d = D();
        System.out.println("listo");
        createTable();
        Statx s = S();
        return new Programax(tablaSimbolos,s);
    }
    
    public Declarax D() {
    if (tknCode == id) {
        String varName = token;
        String nextToken = s.peekToken(); // Usar un método de 'peek' en el scanner para ver el siguiente sin consumirlo

        // Verifica si nextToken es un tipo válido
        if (nextToken.equals("int") || nextToken.equals("float") ||
            nextToken.equals("double") || nextToken.equals("long")) {

            // Continúa como declaración
            eat(id);
            Typex t = T();  // Aquí solo se llama si el nextToken es un tipo
            eat(semi);
            Declarax decl = new Declarax(varName, t);
            tablaSimbolos.addElement(decl);
            D(); // Llamada recursiva para más declaraciones
            return decl;
        }
        // Si no es un tipo, simplemente termina, no es declaración
    }
    // Retorna null si no hay más declaraciones que procesar o no es declaración
    return null;
}


    
    public Typex T() {
        if(tknCode == intx) {
            eat(intx);
            return new Typex("int");
        }
        else if(tknCode == floatx) {
            eat(floatx);
            return new Typex("float");
        }
        else if(tknCode == doublex) {
            eat(doublex);
            return new Typex("double"); 
        }
        else if(tknCode == longx) {
            eat(longx);
            return new Typex("long"); 
        }
        else{
            error(token, "(int / float / double / long)");
            return null;
        }
    }
    
    public Statx S() {
        // S -> if E then S else S | begin S L | id := E | print E
        switch (tknCode) {
            case ifx:
                eat(ifx);
                Expx e1 = E();
                eat(thenx);
                Statx s1 = S();
                eat(elsex);
                Statx s2 = S();
                return new Ifx(e1, s1, s2);
            case beginx:
                eat(beginx);
                Statx s = S();
                L(); // procesa la lista de sentencias hasta end
                return s;
            case id:
                String ident = token;
                eat(id);
                declarationCheck(ident);
                eat(igual);
                Expx e = E();
                return new Asignax(new Idx(ident), e);
            case printx:
                eat(printx);
                Expx ex = E();
                return new Printx(ex);
            default:
                error(token, "(if | begin | id | print)");
                return null;
        }
    }
    public void L() {
        // L -> end | ; S L
        if (tknCode == endx) {
            eat(endx);
        } else if (tknCode == semi) {
            eat(semi);
            S();
            L();
        } else {
            error(token, "(end | ;)");
        }
    }
    
    public Expx E() {
       Idx i1, i2;
       String comp1, comp2;
       
       if(tknCode == id) {
           comp1 = token;
           declarationCheck(token);
           eat(id); 
           i1 = new Idx(token); 
           switch(stringToCode(token)) {
               
               case sum:  
                   comp2 = tokenActual;
                   eat(sum);   eat(id);
                   i2 = new Idx(comp2); //(tokenActual)
                   declarationCheck(comp2);
                   compatibilityCheck(comp1,comp2);
                   byteCode("suma", comp1, comp2);
                   System.out.println("Operación: " + comp1 + "+" + comp2);
                   return new Sumax(i1, i2);
                   
               case igualdad:
                    comp2 = tokenActual;
                    eat(igualdad);
                    eat(id);
                    i2 = new Idx(comp2);
                    declarationCheck(comp2);
                    compatibilityCheck(comp1,comp2);
                    byteCode("igualdad", comp1, comp2);
                    return new Comparax(i1, i2);
                
                case restax:  
                   comp2 = tokenActual;
                   eat(restax);   eat(id);
                   i2 = new Idx(comp2); //(tokenActual)
                   declarationCheck(comp2);
                   compatibilityCheck(comp1,comp2);
                   byteCode("resta", comp1, comp2);
                   System.out.println("Operación: " + comp1 + "-" + comp2);
                   return new Restax(i1, i2);

                case divix:  
                   comp2 = tokenActual;
                   eat(divix);   eat(id);
                   i2 = new Idx(comp2); //(tokenActual)
                   declarationCheck(comp2);
                   compatibilityCheck(comp1,comp2);
                   byteCode("division", comp1, comp2);
                   System.out.println("Operación: " + comp1 + "/" + comp2);
                   return new Divisionx(i1, i2);
                
               case multix:  
                   comp2 = tokenActual;
                   eat(multix);   eat(id);
                   i2 = new Idx(comp2); //(tokenActual)
                   declarationCheck(comp2);
                   compatibilityCheck(comp1,comp2);
                   byteCode("multiplicacion", comp1, comp2);
                   System.out.println("Operación: " + comp1 + "*" + comp2);
                   return new Multiplicacionx(i1, i2);

               default: 
                   error(token, "(+ | - | * | / | ==)");
                   return null;
           }
       }
       else{
           error(token, "(id)");
           return null;
       }
    } //FIN DEL ANÁLISIS SINTÁCTICO
    
    
    
    public void error(String token, String t) {
        switch(JOptionPane.showConfirmDialog(null,
                "Error sintáctico:\n"
                        + "El token:("+ token + ") no concuerda con la gramática del lenguaje,\n"
                        + "se espera: " + t + ".\n"
                        + "¿Desea detener la ejecución?",
                "Ha ocurrido un error",
                JOptionPane.YES_NO_OPTION)) {
            case JOptionPane.NO_OPTION:
                double e = 1.1;
                break;
                
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
        }
    }
    
    public int stringToCode(String t) {
        int codigo = 0;
        switch(t) {
            case "if": codigo=ifx; break;    
            case "then": codigo=thenx; break;
            case "else": codigo=elsex; break;
            case "begin": codigo=beginx; break;
            case "end": codigo=endx; break;
            case "print": codigo=printx; break;
            case ";": codigo=semi; break;
            case "+": codigo=sum; break;
            case ":=": codigo=igual; break;
            case "==": codigo=igualdad; break;
            case "int": codigo=intx; break;
            case "float": codigo=floatx; break;
            case "double": codigo=doublex; break;
            case "long": codigo=longx; break;
            case "-": codigo=restax; break;
            case "*": codigo=multix; break;
            case "/": codigo=divix; break;
            default: codigo=id; break;
        }
        return codigo;
    }
    
    //Métodos para recoger la información de los tokens para luego mostrarla
    public void setLog(String l) {
        if(log == null) {
            log = l + "\n \n";
        }
        else{
            log=log + l + "\n \n";
        }      
    }
    
    public String getLog() {
        return log;
    }
    //-----------------------------------------------
    
    //Recorrido de la parte izquierda del árbol y creación de la tabla de símbolos
    public void createTable() {
        //String[] aux1 = new String[tablaSimbolos.size()];
        //String[] aux2 = new String[tablaSimbolos.size()];
        variable = new String[tablaSimbolos.size()];
        tipo = new String[tablaSimbolos.size()];
        
        //Imprime tabla de símbolos
        System.out.println("-----------------");
        System.out.println("TABLA DE SÍMBOLOS");
        System.out.println("-----------------");
        for(int i=0; i<tablaSimbolos.size(); i++) {
            Declarax dx;
            Typex tx;
            dx = (Declarax)tablaSimbolos.get(i);
            variable[i] = dx.s1;
            tipo[i] = dx.s2.getTypex();
            System.out.println(variable[i] + ": "+ tipo[i]); //Imprime tabla de símbolos por consola.
        }
        
        ArrayUtils.reverse(variable);
        ArrayUtils.reverse(tipo);
        
        System.out.println("-----------------\n");
    }
    
    
    //Verifica las declaraciones de las variables consultando la tabla de símbolos
    public void declarationCheck(String s) {
        boolean valido = false;
        for (int i=0; i<tablaSimbolos.size(); i++) {
            if(s.equals(variable[i])) {
                valido = true;
                break;
            }
        }
        if(!valido) {
            System.out.println("La variable "+ s +  " no está declarada.\nSe detuvo la ejecución.");
             javax.swing.JOptionPane.showMessageDialog(null, "La variable [" + s + "] no está declarada", "Error",
                   javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Chequeo de tipos consultando la tabla de símbolos
    public void compatibilityCheck(String s1, String s2) {
        Declarax elementoCompara1;
        Declarax elementoCompara2;
        System.out.println("CHECANDO COMPATIBILIDAD ENTRE TIPOS ("+s1+", "+s2+"). ");
        boolean termino = false;
        for(int i=0; i<tablaSimbolos.size() ; i++) {
          elementoCompara1 = (Declarax) tablaSimbolos.elementAt(i);
          if(s1.equals(elementoCompara1.s1)) {
            System.out.println("Se encontró el primer elemento en la tabla de símbolos...");
            for(int j=0; j<tablaSimbolos.size() ; j++) {
              elementoCompara2 = (Declarax) tablaSimbolos.elementAt(j);
              if(s2.equals(elementoCompara2.s1)) {
                System.out.println("Se encontró el segundo elemento en la tabla de símbolos...");
                /*if(tipo[i].equals(tipo[j])) {
                  termino = true;
                  break;
                }*/
                if (tipo[i].equals(tipo[j]) ||
                    (tipo[i].equals("int") && tipo[j].equals("long")) ||
                    (tipo[i].equals("long") && tipo[j].equals("int")) ||
                    (tipo[i].equals("float") && tipo[j].equals("double")) ||
                    (tipo[i].equals("double") && tipo[j].equals("float"))
                ) {
                    termino = true;
                    break;
                }
else{
                  termino = true;
                    javax.swing.JOptionPane.showMessageDialog(null, "Incompatibilidad de tipos: "+ elementoCompara1.s1 +" ("
                      + elementoCompara1.s2.getTypex() + "), "+elementoCompara2.s1 +" (" + elementoCompara2.s2.getTypex()
                      +").", "Error",
                      javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                break;
              }
            }
          }
          if(termino) {
            break;
          }
        }
    }
    
    public void byteCode(String tipo, String s1,String s2){
        int pos1=-1, pos2=-1;
        
        for(int i=0; i<variable.length; i++) {
            if(s1.equals(variable[i])) {
                pos1 = i;
            }
            if(s2.equals(variable[i])) {
                pos2 = i;
            }
        }
        
        switch(tipo) {
          case "igualdad":
            ipbc(cntIns + ": iload_"+pos1);
            ipbc(cntIns + ": iload_"+pos2);
            ipbc(cntIns + ": ifne " + (cntIns+4));
            jmp1 = cntBC;
          break;

          case "suma":
            ipbc(cntIns + ": iload_"+pos1);
            ipbc(cntIns + ": iload_"+pos2);
            ipbc(cntIns + ": iadd");
            jmp2 = cntBC;
          break;
          case "resta":
            ipbc(cntIns + ": iload_"+pos1);
            ipbc(cntIns + ": iload_"+pos2);
            ipbc(cntIns + ": ires");
            jmp2 = cntBC;
          break;
          case "multiplicacion":
            ipbc(cntIns + ": iload_"+pos1);
            ipbc(cntIns + ": iload_"+pos2);
            ipbc(cntIns + ": imult");
            jmp2 = cntBC;
          break;
          case "division":
            ipbc(cntIns + ": iload_"+pos1);
            ipbc(cntIns + ": iload_"+pos2);
            ipbc(cntIns + ": idiv");
            jmp2 = cntBC;
          break;
        }
    }
    
    public void byteCode(String tipo, String s1) {
        int pos1 = -1;
        for(int i=0; i<variable.length; i++) {
            if(s1.equals(variable[i])) {
                pos1 = i;
            }
        }
        switch(tipo) {
            case "igual":
                pilaBC[cntBC+3] = cntIns+4 + ": istore_" + pos1;
                cntIns++;
                jmp2 = cntBC;
            break;
        }
    }
    
    public void ipbc(String ins) {
        while(pilaBC[cntBC] != null) {
            cntBC++;
        }
        cntIns++;
        pilaBC[cntBC] = ins;
        cntBC++;
    }
    
    public String getBytecode() {
        String JBC = "";
        for(int i=0; i<pilaBC.length; i++) {
            if(pilaBC[i] != null){
                JBC = JBC + pilaBC[i] + "\n";
            }
        }
        return JBC;
    }    
}
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Principal {
    private static Scanner in = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Iniciar();
        // String codigo = "var1 int ; var2 int ; var1 := var2 + var1 ; print var1 + var2 ;  ";
    }
    private static void Iniciar() throws IOException{
        int op;
        String codigo="";
        do{
            System.out.println("Como se leera el codigo?");
            System.out.println("1. Terminal");
            System.out.println("2. Archivo");
            System.out.println("0. Salir");
            System.out.println("\nIngrese la opcion: ");
            op = in.nextInt();

            switch (op) {
                case 1->{
                    in.nextLine();
                    codigo = LeerCLI();
                }
                case 2->{
                    codigo = LeerArchivo();
                }
                default->{
                    System.out.println("Opcion Invalida");
                    continue;
                }
            }
            if(codigo != null)
                new Parser(codigo);
            System.out.println();
        }while(op != 0);
    }
    private static String LeerCLI(){
        System.out.println(">>>> INGRESE EL CODIGO <<<<\n");
        return in.nextLine();
    }
    private static String LeerArchivo(){
        String codigo = "";
        JFileChooser FileChooser = new JFileChooser();
        FileChooser.setDialogTitle("Selecciona un archivo .java o .txt");

        FileNameExtensionFilter Filtro = new FileNameExtensionFilter("Archivos Java y TXT (*.java, *.txt)", "java", "txt");
        FileChooser.setFileFilter(Filtro);

        int resultado = FileChooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = FileChooser.getSelectedFile();
            try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = lector.readLine()) != null) 
                    codigo += linea+" ";
                lector.close();
                return codigo;
            } catch (IOException e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
        return null;
    }
}
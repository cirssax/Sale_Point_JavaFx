package Algoritmos;

public class Algoritmos 
{
    public boolean ValidacionLetras(String Palabra)
    {//Funcion para validar el ingreso de letras
        int aux;
        boolean validacion=true;
        if ("".equals(Palabra))//Caso en que se ingrese una palabra vacia
            return false;
        else
        {
            for (int i=0; i<Palabra.length(); i++)
            {
                aux=Palabra.charAt(i);//Obtencion del codigo ASCII
                if ((aux<32)||(aux>32 && aux<65) || (aux>90 && aux<97) || (aux>122 && aux<128) || (aux>154 && aux<160) || aux>165) //Segmento de codigos ASCII que no corresponden a Letras mayusculas, minusculas o con tildes especiales
                { 
                    validacion=false;//Caso en que se encuentra caracter distinto
                    break;
                }
            }
            return validacion;
        }
    }
    
    public boolean ValidacionNumerica(String Numeros)
    {//Funcion para validacion de ingreso de numeros
        int aux;
        boolean validacion=true;
        if ("".equals(Numeros))//Caso en que se ingresa palabra vacia
            return false;
        else
        {
            for (int i=0; i<Numeros.length(); i++)
            {
                aux=Numeros.charAt(i);//Obtencion del codigo ASCII
                if (aux<48 || aux>57)//Segmento de codigos ASCII que no son numeros
                {
                    validacion=false;
                    break;
                }   
            }
            return validacion;
        }
    }
    public String ValidacionRut (String Rut)
    {
        String DV = ""; 
        int rut = Integer.parseInt(Rut), sum=0, cont=2, DigitoVerificador;
        while (rut!=0)
        {
            sum = sum + (rut%10)*cont;
            cont++;
            rut=rut/10;
            if(cont==8)
                cont=2;
        }
        DigitoVerificador = 11 - (sum%11);
        if(DigitoVerificador == 11)
        {//Caso en que es -0
            DV = "-0";
        }
        else if(DigitoVerificador == 10)
        {//Caso en que es -K
            DV = "-K";
        }
        else
        {//Caso en que es -1 a -9
            DV = Integer.toString(DigitoVerificador);
            DV = "-"+DV; 
        }  
        return DV;
    }
}

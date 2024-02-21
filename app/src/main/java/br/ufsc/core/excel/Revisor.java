package br.ufsc.core.excel;

import java.util.HashMap;


public class Revisor {
/* Faz ajustes na formatacao de Strings
*/
    
    public static String limpaEspacos(String str){
    /* Retorna uma copia da String str com todos os espacos inciais e finais
        removidos.
        */
        String strFormatada;
        
        try{
            strFormatada = str.trim();
        } catch(Exception e){
            String msg = "Erro ao remover espacos em branco na String: ";
            msg += str;
            System.out.println(msg);
            System.out.println(e);
            
            strFormatada = "ERRO!";
        }
        return strFormatada;        
    }
    
    public static String removeStrNumPonto(String str){
    /* Remove todos os pontos finais "." de uma String str e retorna sua copia.
       */
        String strFormatada;
        
        try{
            strFormatada = str.replace(".", "");            
        } catch(Exception e){
            String msg = "Erro ao substituir virgula por ponto na String: ";
            msg += str;
            System.out.println(msg);
            System.out.println(e);
            
            strFormatada = "ERRO!";
        }
        
        return strFormatada;
    }
    
    public static String formataStrNumVirgula(String str){
    /* Substitui todas as virgulas por ponto final em uma String str e
        retorna sua copia.
        */
        String strFormatada;
        
        try{
            strFormatada = str.replace(',', '.');            
        } catch(Exception e){
            String msg = "Erro ao substituir virgula por ponto na String: ";
            msg += str;
            System.out.println(msg);
            System.out.println(e);
            
            strFormatada = "ERRO!";
        }
        return strFormatada;
    }
    
    public static String formataStrNumPV(String str){
    /* Remove todos os pontos finais "." de uma String str e depois substitui as
        virgulas por ponto final e retorna sua copia.
        */
        String strFormatada;
        
        strFormatada = removeStrNumPonto(str);
        strFormatada = formataStrNumVirgula(strFormatada);
        
        return strFormatada;        
    }
    
    /**
     * Busca a data da publicacao SINAPI na String fornecida, considerando
        uma String cujos ultimos caracteres estao no formato MM/AAAA.
     *
     * Exemplo: "MES DE COLETA: 11/2023" -> "11/2023"
     * 
     * Caso a String nao esteja no formato esperado, retorna "Nao Encontrado"
     * 
     * Retorna mes e ano no formato MM/AAAA
     * @param str
     * @return 
     */
    public static String pegaDataPreco(String str){
        
        String strFormatada;
        
        try{
             // Numero que representa quantos caracteres a retirar ao final da String
            int numUltimosCar = 7;
        
            strFormatada = str.substring(str.length() - numUltimosCar);
        }catch(Exception e){
            strFormatada = "Nao Encontrado";
        }
        return strFormatada;
    }
    
    /**
     * Verifica se a String da Macro Classe informada deve ser substituida,
     * conforme um mapa, e retorna conforme o resultado.
     * 
     * @param mCOriginal
     * @return 
     */
    public static String processaMacroClasse(String mCOriginal){
       
        String str;
        
        HashMap<String, String> mapaSubstituicao = new HashMap<>();
        mapaSubstituicao.put("EQUIPAMENTO (AQUISIÇÃO)", "EQUIPAMENTO");
        mapaSubstituicao.put("EQUIPAMENTO (LOCAÇÃO)", "EQUIPAMENTO");
       
        if(mapaSubstituicao.containsKey(mCOriginal)){
            str = mapaSubstituicao.get(mCOriginal);
        }else{
            str = mCOriginal;
        }
        
        return str;
    }
    
    public static String formataMes(String mes){
        
        HashMap<String, String> mapaMes = new HashMap<>();
        mapaMes.put("01", "JAN");
        mapaMes.put("02", "FEV");
        mapaMes.put("03", "MAR");
        mapaMes.put("04", "ABR");
        mapaMes.put("05", "MAI");
        mapaMes.put("06", "JUN");
        mapaMes.put("07", "JUL");
        mapaMes.put("08", "AGO");
        mapaMes.put("09", "SET");
        mapaMes.put("10", "OUT");
        mapaMes.put("11", "NOV");
        mapaMes.put("12", "DEZ");
        
        return mapaMes.get(mes);
    }
    
}
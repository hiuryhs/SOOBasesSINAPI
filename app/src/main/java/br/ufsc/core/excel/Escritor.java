package br.ufsc.core.excel;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public abstract class Escritor {
 
    /**
     * Retorna true se o parametro num e um double com valor Double.NaN;
     * false se num e um double com valor diferente de null.
     * 
     * @param num
     * @return 
     */
    public boolean verificaDoubleNaN(double num){

    boolean nan = false;

    if (Double.isNaN(num)){
        nan = true;
    }

    return nan;
 }
    
    public void fechaPasta(SXSSFWorkbook pasta, String nomePasta){
        try{
            pasta.close();
        } catch(Exception e){
            StringBuilder msg = new StringBuilder();
            msg.append("Erro ao fechar pasta Excel da base final de dados: ");
            msg.append(nomePasta);
            System.out.println(msg);
            System.out.println(e);              
        }
    }
            
    public void salvaArquivo(SXSSFWorkbook pasta, String endereco){
        // Salva o arquivo xlsx no disco
        Arquivista.salvaXlsx(pasta, endereco);
    }
    
    public void encerraTemporarios(SXSSFWorkbook pasta, String nomePasta){
        // Exclui os dados de linhas do SXSSFWorkbook salvos temporariamente no disco
        pasta.dispose();
        // Remove a pasta SXSSFWorkbook da memoria
        this.fechaPasta(pasta, nomePasta);       
    }
    
    public void escreveString(Cell celula, String str){
        celula.setCellValue(str);
    }  
    
    public void escreveDouble(Cell celula, Double dbl){
        if(this.verificaDoubleNaN(dbl)){
            celula.setCellValue((String) null);
        }else{
            celula.setCellValue(dbl);
        }
    }
           
}
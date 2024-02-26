package br.ufsc.core.excel;

import br.ufsc.core.entity.Publicacao;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public abstract class Coletor {
    
    //  Armazenam as pastas excel abertas    
    private XSSFWorkbook pastaOner;
    private XSSFWorkbook pastaDeso;
    //  Armazena os dados das publicacoes
    private Publicacao dadosPublicacao;
       
    public Coletor(String enderecoDeso, String enderecoOner){
        this.pastaOner = this.abrePastaExcel(enderecoOner);
        this.pastaDeso = this.abrePastaExcel(enderecoDeso);
    }

    public XSSFWorkbook getPastaOner() {
        return pastaOner;
    }

    public XSSFWorkbook getPastaDeso() {
        return pastaDeso;
    }
    
    /**
     * Metodo que define como se dara a abertura de pastas Excel
     * @param endereco 
     * @return  
     */
    public XSSFWorkbook abrePastaExcel(String endereco){
        return Arquivista.abreXlsx(endereco);
    }
    
    /**
     * Metodo que de fato atribui os valores aos atributos que armazenam as 
     * pastas Excel
     * @param enderecoDeso
     * @param enderecoOner 
     */
    public void abrePastasExcel(String enderecoDeso, String enderecoOner){
        this.pastaOner = this.abrePastaExcel(enderecoOner);
        this.pastaDeso = this.abrePastaExcel(enderecoDeso);
    }
    
    public XSSFSheet selecionaPlanilha(XSSFWorkbook pasta, String nomePlanilha){
        return pasta.getSheet(nomePlanilha);
    }
    
    public XSSFSheet selecionaPlanilha(XSSFWorkbook pasta, int indicePlanilha){
        return pasta.getSheetAt(indicePlanilha);
    }
    
    public Publicacao getDadosPublicacao() {
        return dadosPublicacao;
    }

    public void setDadosPublicacao(Publicacao dadosPublicacao) {
        this.dadosPublicacao = dadosPublicacao;
    }
    /**
    Verifica se as Strings dataPrecoDeso e dataPrecoOner sao validas.
    Retorna 1 se validas; 0 se datas diferentes; -1 se "Nao Encontrado".
    Recebe as datas no formato MM/AAAA.
    * @param dataPreco1
    * @param dataPreco2
    * @return 
    */
    public int verificaDatasPreco(String dataPreco1, String dataPreco2){
        int codigo;
        
        if(dataPreco1.equals("Nao Encontrado") || dataPreco2.equals("Nao Encontrado")){
            codigo = -1;
        }else if (dataPreco1.equals(dataPreco2)){
            codigo = 1;
        }else{
            codigo = 0;
        }
        
        return codigo;         
    }
    
    public String pegaValorCelStr (XSSFCell celula){
        
        String valor;
        
        try{
            valor = celula.getStringCellValue();
            valor = Revisor.limpaEspacos(valor);
        } catch(Exception e){
            String msg = "Erro ao recuperar valor string na celula: ";
            msg += "Linha " + celula.getRowIndex() + " Coluna " + celula.getColumnIndex();
            System.out.println(msg);
            System.out.println(e);
            
            valor = "ERRO!";
            
            System.exit(1);
        }
        
        return valor;
    }
    
    public int pegaValorCelInt(XSSFCell celula){
        
        String valor = pegaValorCelStr(celula);
        int num;
        
        try{
            num = Integer.parseInt(valor);
        }catch(Exception e){
            String msg = "Erro ao converter valor String para int na celula: ";
            msg += "Linha " + celula.getRowIndex() + " Coluna " + celula.getColumnIndex();
            System.out.println(msg);
            System.out.println(e);            
            
            num = -1;
            
            System.exit(1);
        }
        
        return num;

    }
    
    /**
     * Verifica se uma celula e:
     * - Nula;
     * - Possui uma String vazia;
     * - Possui apenas espacos em branco.
     * 
     * Retorna true nesses casos.
     * 
     * @param celula
     * @return 
     */
    public boolean celulaVazia(XSSFCell celula){
        
        boolean vazia = false;
        
        if (celula== null || celula.getCellType() == CellType.BLANK) {
            vazia = true;
        }else if(celula.getCellType() == CellType.STRING) {
            // Verifica se a String e vazia ou possui apenas espacos em branco
            vazia = celula.getStringCellValue().trim().isEmpty();

        }
       
        return vazia;
    }
    
    public double pegaValorCelDouble(XSSFCell celula){
        
        double num;
        
        try{
            String valor = celula.getStringCellValue();
            valor = Revisor.limpaEspacos(valor);
            valor = Revisor.formataStrNumPV(valor);
            num = Double.parseDouble(valor);
        }catch(NumberFormatException nfe){
            String msg = "Ao converter para double, a celula esta vazia: ";
            msg += "Linha " + celula.getRowIndex() + " Coluna " + celula.getColumnIndex();
            System.out.println(msg);
            System.out.println(nfe);            
            
            num = 0;
        }catch(NullPointerException npe){
            String msg = "Erro ao converter valor String para double na celula: ";
            msg += "Linha " + celula.getRowIndex() + " Coluna " + celula.getColumnIndex();
            System.out.println(msg);
            System.out.println(npe);            
            
            num = -1;
            
            System.exit(1);
        }catch(IllegalStateException ile){
            num = celula.getNumericCellValue();
        }
        
        return num;        
    }
    
    public void fechaPastaExcel(XSSFWorkbook pasta, String nomePasta){
        try{
            pasta.close();
        } catch(Exception e){
            String msg = "Erro ao fechar pasta Excel de: " + nomePasta;
            System.out.println(msg);
            System.out.println(e);              
        }
    }
}

package br.ufsc.core.excel;


import br.ufsc.core.entity.Insumo;
import br.ufsc.core.entity.Publicacao;
import java.util.Iterator;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ColetorInsumo extends Coletor{
    
    // PARA PLANILHAS DE PRE�OS
    //  Numero de linhas ate os dados, pulando todos os cabecalhos
    private static final int LININSUCAB = 7;
    //  Numero da linha onde se encontra a data do preco
    private static final int LINDATAPRECO = 2;    
    //  Constantes do I�ndice das colunas para insumos
    private static final int COLINSUCOD = 0;
    private static final int COLINSUDESC = 1;
    private static final int COLINSUUN = 2;
    private static final int COLINSUORIG = 3;
    private static final int COLINSUPRECO = 4;
    // PARA PLANILHA DA FAMILIA DE INSUMOS
    private static final int LINFAMCAB = 3;
    private static final int COLFAMCOD = 1;
    private static final int COLFAMCLASS = 6;
    // Armazena a pasta Excel de familia de Insumos
    private XSSFWorkbook pastaFam;
    //  Armazena o mapa com os dados dos insumos
    private TreeMap<Double, Insumo> insumos;
    
    public ColetorInsumo(String enderecoDeso, String enderecoOner, String enderecoFamilia){
        super(enderecoDeso, enderecoOner);
        this.pastaFam = super.abrePastaExcel(enderecoFamilia);
    }

    public void setInsumos(Insumo insumo){
        insumos.put(insumo.getCodigo(), insumo);
    }
    
    public TreeMap<Double, Insumo> getInsumos(){
        return this.insumos;
    }

    public XSSFWorkbook getPastaFam() {
        return pastaFam;
    }
    
    public void abrePastasExcel(String enderecoDeso, String enderecoOner, String enderecoFamilia){
        super.abrePastasExcel(enderecoOner, enderecoDeso);
        this.pastaFam = super.abrePastaExcel(enderecoFamilia);
    }    
    
    /**
     * Busca os dados dos insumos nas bases SINAPI de precos em Excel .xlsx,
     * e adiciona para um mapa TreeMap, atributo da classe (composicoes).
     * 
     * @return  int
     * 1 se sucesso e datas da publicacao validas;
     * -1 se sucesso e datas nao encontradas;
     * 0 se datas invalidas (e nao executa a base de dados)
     */
    public int buscaInsumosPrecos(){

        int codigoRetorno, validaData = 0;
        
//  Seleciona a primeira (e unica) planilha da pasta
        XSSFSheet planOner = super.selecionaPlanilha(super.getPastaOner(), 0);
        XSSFSheet planDeso = super.selecionaPlanilha(super.getPastaDeso(), 0);
        
//  Cria os iteradores que irao percorrer as linhas da planilha
        Iterator<Row> andaLinhaOner = planOner.iterator();
        Iterator<Row> andaLinhaDeso = planDeso.iterator();
        
// Seleciona a primeira linha da planilha
        XSSFRow linhaOner = (XSSFRow) andaLinhaOner.next();
        XSSFRow linhaDeso = (XSSFRow) andaLinhaDeso.next();
        
//  Navega ate o cabecalho da planilha
        XSSFCell celAtual = linhaOner.getCell(0);
        
        while(linhaDeso.getRowNum() < LININSUCAB - 1){
            linhaDeso = (XSSFRow) andaLinhaDeso.next();
            linhaOner = (XSSFRow) andaLinhaOner.next();
            
//            Recupera as informacoes da celula que contem a data dos precos,
//            no formato MM/AAAA
            if (linhaDeso.getRowNum() == LINDATAPRECO){
                String dataPrecoDeso ="", dataPrecoOner="";
//              Pega os valores das celulas               
                celAtual = linhaDeso.getCell(0);
                if(celAtual != null){
                    dataPrecoDeso = celAtual.getStringCellValue();
                }
                
                celAtual = linhaOner.getCell(0);
                if(celAtual != null){
                    dataPrecoOner = celAtual.getStringCellValue();
                }

//              Formata os valores das celulas retornando a data no formato MM/AAAA ou "Nao Encontrado"
                dataPrecoDeso = Revisor.pegaDataPreco(dataPrecoDeso);
                dataPrecoOner = Revisor.pegaDataPreco(dataPrecoOner);

//              Verifica se as datas sao iguais ou nao ou se nao foram encontradas
                validaData = super.verificaDatasPreco(dataPrecoDeso, dataPrecoOner);
                
                // Salva os dados num objeto de Publicacao
                Publicacao publ;
                if(validaData == 1){
                    publ = new Publicacao(dataPrecoOner);
                }else{
                    publ = new Publicacao("Nao Encontrado");
                }
                super.setDadosPublicacao(publ);
            }
        }
        if (validaData == 1 || validaData == -1){            
    //     Itera sobre cada linha das planilhas buscando as informacoes relevantes
    //     da insumo;  Cria uma instancia de Insumo; Adiciona essa instancia
    //     a um mapa.
            this.insumos = new TreeMap<Double, Insumo>();
            
            // Inicializa as variaveis para criacao de um insumo
            Insumo insumo;         
            double codigoInsu;
            String descricao;
            String unidade;
            String origem;
            double custoOner;
            double custoDeso;
            
            // Seleciona a celula da primeira linha de dados da tabela
            celAtual = linhaOner.getCell(0);         
            
            while(andaLinhaOner.hasNext() && !super.celulaVazia(celAtual)){
    //          Avanca para a linha seguinte
                linhaOner = (XSSFRow) andaLinhaOner.next();
                linhaDeso = (XSSFRow) andaLinhaDeso.next();
                
                insumo = new Insumo();

    //          Navega pelas celulas com os dados relevantes e recupera seus valores            
                celAtual = linhaOner.getCell(COLINSUCOD);
                if(!super.celulaVazia(celAtual)){
                    codigoInsu = super.pegaValorCelDouble(celAtual);

                    celAtual = linhaOner.getCell(COLINSUDESC);
                    descricao = super.pegaValorCelStr(celAtual);

                    celAtual = linhaOner.getCell(COLINSUUN);
                    unidade = super.pegaValorCelStr(celAtual);

                    celAtual = linhaOner.getCell(COLINSUORIG);
                    origem = super.pegaValorCelStr(celAtual);

                    celAtual = linhaOner.getCell(COLINSUPRECO);
                    custoOner = super.pegaValorCelDouble(celAtual);
                    celAtual = linhaDeso.getCell(COLINSUPRECO);
                    custoDeso = super.pegaValorCelDouble(celAtual);

        //          Salva as informacoes necessarias na instancia da composicao       
                    insumo.setCodigo(codigoInsu);
                    insumo.setDescricao(descricao);
                    insumo.setUnidade(unidade);
                    insumo.setOrigem(origem);
                    insumo.setCustoOner(custoOner);
                    insumo.setCustoDeso(custoDeso);

                    this.setInsumos(insumo);
                }
            }
            // Busca os dados da MACRO-CLASSE dos insumos
            this.buscaInsumoFam();
        }    
    // Fecha os XSSFWorkbooks das pastas excel
        super.fechaPastaExcel(super.getPastaOner(), "Insumos Onerados");
        super.fechaPastaExcel(super.getPastaDeso(), "Insumos Desonerados");
        
        codigoRetorno = validaData;
        

        
        return codigoRetorno;
    }
    
   /**
    * Busca o dado MACRO CLASSE dos insumos na planilha de familia de insumos e 
    * o armazena no respectivo atributo dos insumos coletados.
    */
    public void buscaInsumoFam(){
        //  Seleciona a primeira (e unica) planilha da pasta
        XSSFSheet planFam = super.selecionaPlanilha(this.getPastaFam(), 0);
        
        // Cria o iterador que ira percorrer as linhas da planilha
        Iterator<Row> andaLinhaFam = planFam.iterator();
        
        // Seleciona a primeira linha da planilha
        XSSFRow linhaFam = (XSSFRow) andaLinhaFam.next();
        
        //  Pula os cabecalhos da planilha
        while(linhaFam.getRowNum() < LINFAMCAB - 1){
            linhaFam = (XSSFRow) andaLinhaFam.next();
        }
        XSSFCell celAtual = linhaFam.getCell(0);

        // Declara as variaveis para armazenar o codigo e a macro classe
        double codigoFam;
        String macroClasse;
            
        while(andaLinhaFam.hasNext() && !super.celulaVazia(celAtual)){
            // Avanca para a linha seguinte
            linhaFam = (XSSFRow) andaLinhaFam.next();
            
            // Navega pelas celulas com os dados relevantes e recupera seus valores            
            celAtual = linhaFam.getCell(COLFAMCOD);
            if(!super.celulaVazia(celAtual)){
                codigoFam = super.pegaValorCelDouble(celAtual);

                celAtual = linhaFam.getCell(COLFAMCLASS);
                macroClasse = super.pegaValorCelStr(celAtual);

                // Armazena o dado MACRO CLASSE no respectivo atributo de cada Insumo
                // armazenado no mapa de Insumo
                Insumo insumo = this.insumos.get(codigoFam);

                if(insumo != null){
                    insumo.setMacroClasse(Revisor.processaMacroClasse(macroClasse));
                }else{
                    String msg = "Insumo: " + codigoFam + " nao encontrado na base Familia de Insumos";
                    System.out.println(msg);
                }
            }
        }
    // Fecha os XSSFWorkbooks das pastas excel
        super.fechaPastaExcel(this.getPastaFam(), "Familia de Insumos");
    }
    
}
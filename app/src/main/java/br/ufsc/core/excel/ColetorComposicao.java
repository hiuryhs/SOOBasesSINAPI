package br.ufsc.core.excel;


import br.ufsc.core.entity.Composicao;
import br.ufsc.core.entity.Elemento;
import br.ufsc.core.entity.Publicacao;
import java.util.Iterator;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;


public class ColetorComposicao extends Coletor{
/* Classe responsavel por coletar os dados da planilha SINAPI e armazena-los.
    */   
    
//  Numero de linhas ate os dados, pulando todos os cabecalhos
    private static final int LINHASCABECALHO = 7;
//  Numero da linha onde se encontra a data do preco
    private static final int LINHADATAPRECO = 2;    
//  Constantes do I�ndice das colunas para composicoes
    private static final int COLCOMPCOD = 6;
    private static final int COLCOMPDESCR = 7;
    private static final int COLCOMPUN = 8;
    private static final int COLCOMPCMO = 19;
    private static final int COLCOMPCMAT = 21;
    private static final int COLCOMPCEQ = 23;
    private static final int COLCOMPCSER = 25;
    private static final int COLCOMPCOUT = 27;
//  Constantes do I�ndice das colunas para elementos  
    private static final int COLELPAI = 6;
    private static final int COLELPAIDESC = 7;
    private static final int COLELPAIUN = 8;
    private static final int COLELTIPO = 11;
    private static final int COLELCOD = 12;
    private static final int COLELORIG = 15;
    private static final int COLELCOEF =16;
//  Armazena o mapa com os dados das composicoes
    private TreeMap<Double, Composicao> composicoes;
    
    public ColetorComposicao(String enderecoDeso, String enderecoOner){
        super(enderecoDeso, enderecoOner);
    }
    
    public void setComposicoes(Composicao composicao){
        this.composicoes.put(composicao.getCodigo(), composicao);
    }
    
    public TreeMap<Double, Composicao> getComposicoes(){
        return this.composicoes;
    }

       
    /**
     * Busca os dados de composicoes e elementos nas bases de dados SINAPI em Excel .xlsx,
     * e adiciona para um mapa TreeMap, atributo da classe (composicoes).
     * 
     * @return  int
     * 1 se sucesso e datas da publicacao validas;
     * -1 se sucesso e datas nao encontradas;
     * 0 se datas invalidas (e nao executa a base de dados)
     */
    public int buscaComposicoes(){

        int codigoRetorno, validaData = 0;
        
        //  Seleciona a primeira (e unica) planilha de cada pasta
        XSSFSheet planOner = super.selecionaPlanilha(super.getPastaOner(), 0);
        XSSFSheet planDeso = super.selecionaPlanilha(super.getPastaDeso(), 0);
        
        //  Cria os iteradores que irao percorrer as linhas de cada planilha
        Iterator<Row> andaLinhaOner = planOner.iterator();
        Iterator<Row> andaLinhaDeso = planDeso.iterator();
        
        // Seleciona a primeira linha de cada planilha
        XSSFRow linhaOner = (XSSFRow) andaLinhaOner.next();
        XSSFRow linhaDeso = (XSSFRow) andaLinhaDeso.next();
        
        //  Pula os cabecalhos de cada planilha
        XSSFCell celAtual = linhaOner.getCell(0);
        
        while(linhaOner.getRowNum() < LINHASCABECALHO){
            linhaOner = (XSSFRow) andaLinhaOner.next();
            linhaDeso = (XSSFRow) andaLinhaDeso.next();
            
            // Recupera as informacoes da celula que contem a data dos precos,
            // no formato MM/AAAA
            if (linhaDeso.getRowNum() == LINHADATAPRECO){
                
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

                // Formata os valores das celulas retornando a data no formato MM/AAAA ou "Nao Encontrado"
                dataPrecoDeso = Revisor.pegaDataPreco(dataPrecoDeso);
                dataPrecoOner = Revisor.pegaDataPreco(dataPrecoOner);

                // Verifica se as datas sao iguais ou nao ou se nao foram encontradas
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
    //     de cada composicao e elemento e adiciona a composicao a uma lista
            this.composicoes = new TreeMap<Double, Composicao>();

            // Declara as variaveis para criacao de uma Composicao
            Composicao composicao;           
            double codigoComp;
            String descricao;
            String unidade;
            double custoMoDeso;
            double custoMatDeso;
            double custoEqDeso;
            double custoSerDeso;
            double custoOutDeso;
            double custoMoOner;
            double custoMatOner;
            double custoEqOner;
            double custoSerOner;
            double custoOutOner;
            // Declara as variaveis para criacao de um Elemento
            double codigoAtual;
            Elemento elemento;
            String paiDescricao;
            String paiUnidade;
            String tipo;
            double codigoElem;
            String origem;
            double coeficiente;
            
            // Seleciona a celula da primeira linha de dados da tabela
            celAtual = linhaOner.getCell(0);
            
            while(andaLinhaOner.hasNext() && !super.celulaVazia(celAtual)){

                composicao = new Composicao();

    //          Navega pelas celulas com as informacoes relevantes e recupera seus valores            
                celAtual = linhaOner.getCell(COLCOMPCOD);
                codigoComp = super.pegaValorCelDouble(celAtual);

                celAtual = linhaOner.getCell(COLCOMPDESCR);
                descricao = super.pegaValorCelStr(celAtual);

                celAtual = linhaOner.getCell(COLCOMPUN);
                unidade = super.pegaValorCelStr(celAtual);

                celAtual = linhaOner.getCell(COLCOMPCMO);
                custoMoOner = super.pegaValorCelDouble(celAtual);
                celAtual = linhaDeso.getCell(COLCOMPCMO);
                custoMoDeso = super.pegaValorCelDouble(celAtual);

                celAtual = linhaOner.getCell(COLCOMPCMAT);
                custoMatOner = super.pegaValorCelDouble(celAtual);
                celAtual = linhaDeso.getCell(COLCOMPCMAT);
                custoMatDeso = super.pegaValorCelDouble(celAtual);

                celAtual = linhaOner.getCell(COLCOMPCEQ);
                custoEqOner = super.pegaValorCelDouble(celAtual);
                celAtual = linhaDeso.getCell(COLCOMPCEQ);
                custoEqDeso = super.pegaValorCelDouble(celAtual);

                celAtual = linhaOner.getCell(COLCOMPCSER);
                custoSerOner = super.pegaValorCelDouble(celAtual);
                celAtual = linhaDeso.getCell(COLCOMPCSER);
                custoSerDeso = super.pegaValorCelDouble(celAtual);

                celAtual = linhaOner.getCell(COLCOMPCOUT);
                custoOutOner = super.pegaValorCelDouble(celAtual);
                celAtual = linhaDeso.getCell(COLCOMPCOUT);
                custoOutDeso = super.pegaValorCelDouble(celAtual);

    //          Salva as informacoes necessarias na instancia da composicao       
                composicao.setCodigo(codigoComp);
                composicao.setDescricao(descricao);
                composicao.setUnidade(unidade);
                composicao.setCustoMoDeso(custoMoDeso);
                composicao.setCustoMoOner(custoMoOner);
                composicao.setCustoMatDeso(custoMatDeso);
                composicao.setCustoMatOner(custoMatOner);
                composicao.setCustoEqOner(custoEqOner + custoOutOner + custoSerOner);
                composicao.setCustoEqDeso(custoEqDeso + custoOutDeso + custoSerDeso);

    //          Avanca para a linha seguinte, onde se encontra o primeiro elemento da composicao
                linhaOner = (XSSFRow) andaLinhaOner.next();
                linhaDeso = (XSSFRow) andaLinhaDeso.next();

    //          Recupera o codigo do elemento, para compara-lo com o da composicao pai            
                celAtual = linhaOner.getCell(COLCOMPCOD);
                codigoAtual = super.pegaValorCelDouble(celAtual);

    //          Itera pelos elementos da composicao e os adiciona a sua lista de elementos
                while(codigoAtual == codigoComp && !super.celulaVazia(celAtual)){

                    elemento = new Elemento();

                    celAtual = linhaOner.getCell(COLELPAIDESC);
                    paiDescricao = super.pegaValorCelStr(celAtual);

                    celAtual = linhaOner.getCell(COLELPAIUN);
                    paiUnidade = super.pegaValorCelStr(celAtual);

                    celAtual = linhaOner.getCell(COLELTIPO);
                    tipo = super.pegaValorCelStr(celAtual);

                    celAtual = linhaOner.getCell(COLELCOD);
                    codigoElem = super.pegaValorCelDouble(celAtual);

                    celAtual = linhaOner.getCell(COLELORIG);
                    origem = super.pegaValorCelStr(celAtual);

                    celAtual = linhaOner.getCell(COLELCOEF);
                    coeficiente = super.pegaValorCelDouble(celAtual);

                    elemento.setPaiCodigo(codigoAtual);
                    elemento.setPaiDescricao(paiDescricao);
                    elemento.setPaiUnidade(paiUnidade);
                    elemento.setTipo(tipo);
                    elemento.setCodigo(codigoElem);
                    elemento.setOrigem(origem);
                    elemento.setCoeficiente(coeficiente);

                    composicao.setFilhos(elemento);

    //              Verifica se ainda nao chegou na ultima linha
                    if (andaLinhaOner.hasNext()){
                        linhaOner = (XSSFRow) andaLinhaOner.next();
                        linhaDeso = (XSSFRow) andaLinhaDeso.next();

                        celAtual = linhaDeso.getCell(COLCOMPCOD);
                        if(!super.celulaVazia(celAtual)){
                            codigoAtual = super.pegaValorCelInt(celAtual);
                        }
                    } else{
                        break;
                    }    
                }
                this.setComposicoes(composicao);
            }
        }    
    // Fecha os XSSFWorkbooks das pastas excel
        super.fechaPastaExcel(super.getPastaOner(), "Servicos Onerados");
        super.fechaPastaExcel(super.getPastaDeso(), "Servicos Desonerados");
        
        codigoRetorno = validaData;
        
        return codigoRetorno;
    }    
}
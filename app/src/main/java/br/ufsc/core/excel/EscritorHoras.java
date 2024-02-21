package br.ufsc.core.excel;


import br.ufsc.core.entity.Composicao;
import br.ufsc.core.entity.Elemento;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class EscritorHoras extends Escritor{

    private static final String NOMEPASTAEXCEL = "BASE_COMPSINT_OD_HORAS_";
    private static final String NOMEPLANILHA = "COMP";
    private static final String[] CABECALHO = {
    "CODIGO DA COMPOSICAO", 
    "DESCRICAO DA COMPOSICAO", 
    "UNIDADE", 
    "TIPO ITEM", 
    "CODIGO ITEM", 
    "ORIGEM DE PRECO ITEM", 
    "COEFICIENTE", 
    "CUSTO MAO DE OBRA DESONERADO", 
    "CUSTO MATERIAL DESONERADO", 
    "CUSTO EQUIPAMENTO DESONERADO",
    "CUSTO MAO DE OBRA ONERADO",
    "CUSTO MATERIAL ONERADO",
    "CUSTO EQUIPAMENTO ONERADO"};
    private static final int NUMLINDADOS = 1; //Numero de linhas a pular entre o cabecalho e os dados
    
    public void escreveLinha(
            SXSSFRow linha,
            double codigoComposicao,
            String descricaoComposicao,
            String unidade,
            String tipoItem,
            double codigoItem,
            String origemDePrecoItem,
            double coeficiente,
            double custoMaoDeObraDeso,
            double custoMaterialDeso,
            double custoEquipamentoDeso,
            double custoMaoDeObraOner,
            double custoMaterialOner,
            double custoEquipamentoOner
    ){
        
        int coluna = 0;
        
        // Declara e instancia o objeto da que representa a primeira celula da linha
        Cell celula = linha.createCell(coluna);    
        // Escreve o primeiro valor na celula
        super.escreveDouble(celula, codigoComposicao);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, descricaoComposicao);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, unidade);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, tipoItem);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, codigoItem);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, origemDePrecoItem);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, coeficiente);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoMaoDeObraDeso);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoMaterialDeso);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoEquipamentoDeso);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoMaoDeObraOner);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoMaterialOner);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoEquipamentoOner);
    }
    

    public void criaBaseHoras(TreeMap<Double, Composicao> composicoes, String data, String diretorio){
        
//      Cria uma pasta Excel
        SXSSFWorkbook pasta = new SXSSFWorkbook(100);
        
//      Cria uma planilha na pasta
        SXSSFSheet planilha = pasta.createSheet(NOMEPLANILHA);

//      Cria os contadore dos indices para criacao das linhas e colunas
        int numLinha = 0, coluna = 0;
        
//      Cria a primeira linha da planilha:
        SXSSFRow linhaAtual = planilha.createRow(numLinha);
        
//      Escreve o cabecalho:
        for (String cabecalho : CABECALHO){
       
            Cell celula = linhaAtual.createCell(coluna++);
            celula.setCellValue(cabecalho);
        }
        
//      Adiciona espacos em branco entre o cabecalho e os dados
        numLinha = 1 + NUMLINDADOS;
        
//      Preenche as linhas com os dados das composicoes
        for (Composicao composicao : composicoes.values()){
            // Cria uma nova linha
            linhaAtual = planilha.createRow(numLinha++);
            // Escreve os dados na linha 
            this.escreveLinha(
            linhaAtual,
            composicao.getCodigo(),
            composicao.getDescricao(),
            composicao.getUnidade(),
            null,
            Double.NaN,
            null,
            Double.NaN,
            composicao.getCustoMoDeso(),
            composicao.getCustoMatDeso(),
            composicao.getCustoEqDeso(),
            composicao.getCustoMoOner(),
            composicao.getCustoMatOner(),
            composicao.getCustoEqOner()
            );

  
//          Adiciona os elementos por cada composicao        
            for(Elemento elemento : composicao.getFilhos().values()){
            
                linhaAtual = planilha.createRow(numLinha++);
                
                this.escreveLinha(
                linhaAtual,
                elemento.getPaiCodigo(),
                elemento.getPaiDescricao(),
                elemento.getPaiUnidade(),
                elemento.getTipo(),
                elemento.getCodigo(),
                elemento.getOrigem(),
                elemento.getCoeficiente(),
                Double.NaN,
                Double.NaN,
                Double.NaN,
                Double.NaN,
                Double.NaN,
                Double.NaN
                );
            }           
        }
        
        // Cria o endereco completo para salvamento      
        String endereco = diretorio + "\\"+ this.NOMEPASTAEXCEL + data + ".xlsx";
        
        super.salvaArquivo(pasta, endereco);
        
        super.encerraTemporarios(pasta, "Horas.");
        
    }
    
}
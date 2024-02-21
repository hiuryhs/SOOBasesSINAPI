package br.ufsc.core.excel;


import br.ufsc.core.entity.Composicao;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class EscritorComposicao extends Escritor{

    private static final String NOMEPASTAEXCEL = "BASE_COMPSINT_OD_";
    private static final String[] CABECALHO1 = {
        "CODIGO DA COMPOSICAO",
        "DESCRICAO DA COMPOSICAO",
        "UNIDADE",
        "CUSTO MAO DE OBRA - D",
        "CUSTO MATERIAL - D",
        "CUSTO EQUIPAMENTO - D",
        "CUSTO MAO DE OBRA - O",
        "CUSTO MATERIAL - O",
        "CUSTO EQUIPAMENTO - O"
    };
    private static final String[] CABECALHO2 = {
        "",
        "",
        "",
        "DESONERADO",
        "DESONERADO",
        "DESONERADO",
        "ONERADO",
        "ONERADO",
        "ONERADO",
    };
    
    public void escreveLinha(
            SXSSFRow linha,
            double codigoComposicao,
            String descricao,
            String unidade,
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
        super.escreveString(celula, descricao);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, unidade);
        
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
    
    public void criaBaseComp(TreeMap<Double, Composicao> composicoes, String data, String diretorio){
        
//      Cria uma pasta Excel
        SXSSFWorkbook pasta = new SXSSFWorkbook(100);
        
//      Cria uma planilha na pasta
        SXSSFSheet planilha = pasta.createSheet(data);

//      Cria os contadore dos indices para criacao das linhas e colunas
        int numLinha = 0, coluna = 0;
        
//      Cria a primeira linha da planilha:
        SXSSFRow linhaAtual = planilha.createRow(numLinha);
        
//      Escreve o cabecalho 1:
        for (String cabecalho : CABECALHO1){
            Cell celula = linhaAtual.createCell(coluna++);
            celula.setCellValue(cabecalho);
        }
        
        // Escreve o cabecalho 2
        linhaAtual = planilha.createRow(++numLinha);
        coluna = 0;
        
        for (String cabecalho : CABECALHO2){
            Cell celula = linhaAtual.createCell(coluna++);
            celula.setCellValue(cabecalho);

        }
        
//      Preenche as linhas com os dados das composicoes
        for (Composicao composicao : composicoes.values()){
            
            linhaAtual = planilha.createRow(++numLinha);
            
            this.escreveLinha(
            linhaAtual,
            composicao.getCodigo(),
            composicao.getDescricao(),
            composicao.getUnidade(),
            composicao.getCustoMoDeso(),
            composicao.getCustoMatDeso(),
            composicao.getCustoEqDeso(),
            composicao.getCustoMoOner(),
            composicao.getCustoMatOner(),
            composicao.getCustoEqOner()
            );
        }
        
        // Cria o endereco completo para salvamento      
        String endereco = diretorio + "\\"+ NOMEPASTAEXCEL + data + ".xlsx";
        
        super.salvaArquivo(pasta, endereco);
        
        super.encerraTemporarios(pasta, "Composicoes.");
        
    }
    
}
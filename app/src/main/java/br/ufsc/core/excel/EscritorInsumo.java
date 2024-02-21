package br.ufsc.core.excel;


import br.ufsc.core.entity.Insumo;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class EscritorInsumo extends Escritor{

    private static final String NOMEPASTAEXCEL = "BASE_INSUMOS_OD_";
    private static final String[] CABECALHO1 = {
        "CODIGO",
        "DESCRICAO DO INSUMO",
        "UNIDADE DE MEDIDA",
        "PRECO MEDIANO R$ - D",
        "PRECO MEDIANO R$ - O",
        "ORIGEM DO PRECO",
        "MACRO CLASSE"
    };
    private static final String[] CABECALHO2 = {
        "",
        "",
        "",
        "DESONERADO",
        "ONERADO",
        "",
        ""
    };

    
    public void escreveLinha(
            SXSSFRow linha,
            double codigoInsumo,
            String descricao,
            String unidade,
            double custoDeso,
            double custoOner,
            String origem,
            String macroClasse
    ){
        int coluna = 0;
        
        // Declara e instancia o objeto da que representa a primeira celula da linha
        Cell celula = linha.createCell(coluna);    
        // Escreve o primeiro valor na celula
        super.escreveDouble(celula, codigoInsumo);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, descricao);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, unidade);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoDeso);
        
        celula = linha.createCell(++coluna);
        super.escreveDouble(celula, custoOner);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, origem);
        
        celula = linha.createCell(++coluna);
        super.escreveString(celula, macroClasse);
    }
    
    public void criaBaseInsu(TreeMap<Double, Insumo> insumos, String data, String diretorio){
        
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
        for (Insumo insumo : insumos.values()){
            
            linhaAtual = planilha.createRow(++numLinha);
            
            this.escreveLinha(
                linhaAtual,
                insumo.getCodigo(),
                insumo.getDescricao(),
                insumo.getUnidade(),
                insumo.getCustoDeso(),
                insumo.getCustoOner(),
                insumo.getOrigem(),
                insumo.getMacroClasse()
            );
        }
        
        // Cria o endereco completo para salvamento      
        String endereco = diretorio + "\\"+ NOMEPASTAEXCEL + data + ".xlsx";
        
        super.salvaArquivo(pasta, endereco);
        
        super.encerraTemporarios(pasta, "Insumos.");
        
    }
    
}
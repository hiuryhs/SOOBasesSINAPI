package br.ufsc.core.excel;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Arquivista {
/* Classe responsavel por gerenciar a abertura e salvamento de arquivos e diretorios.
    */    
    public static XSSFWorkbook abreXlsx(String endereco){
        
        XSSFWorkbook arquivoExcel;
        Path path;
        File arquivo;
        
        try{
            path = Path.of(endereco);
            arquivo = path.toFile();
            arquivoExcel = new XSSFWorkbook(arquivo);
        }catch(InvalidPathException iPE){
            System.out.println("Erro ao identificar o endereco(abrir) :" + endereco);
            System.out.println("Excecao " + iPE);            
            arquivoExcel = null;
        }catch(UnsupportedOperationException uOE){
            System.out.println("Erro ao criar o arquivo para o endereco(abrir) :" + endereco);
            System.out.println("Excecao " + uOE);
            arquivoExcel = null;
        }catch(IOException | InvalidFormatException e){
            System.out.println("Erro ao construir XSSFWworkbook a partir do endereco :" + endereco);
            System.out.println("Excecao " + e);
            arquivoExcel = null;
        }
        
        return arquivoExcel;
    }
    
    public static void salvaXlsx(SXSSFWorkbook pasta, String endereco){
        
        Path path;
        File arquivo;
        OutputStream saida;
        
        try{
            path = Path.of(endereco);
            arquivo = path.toFile();
            saida = new FileOutputStream(arquivo);
        }catch(InvalidPathException iPE){
            System.out.println("Erro ao identificar o endereco(salvar) :" + endereco);
            System.out.println("Excecao " + iPE);       
            saida = null;
        }catch(UnsupportedOperationException uOE){
            System.out.println("Erro ao criar o arquivo para o endereco(salvar) :" + endereco);
            System.out.println("Excecao " + uOE);
            saida = null;
        }catch(IOException iOE){
            System.out.println("Erro ao criar a saida para o endereco(salvar) :" + endereco);
            System.out.println("Excecao " + iOE);
            saida = null;
        }
        
        try{
            pasta.write(saida);
        }catch(IOException iOE){
            System.out.println("Erro ao salvar o arquivo de endereco :" + endereco);
            System.out.println("Excecao " + iOE);
        }
        
        try{
            saida.close();
        }catch(IOException iOE){
            System.out.println("Erro ao fechar a saida do endereco :" + endereco);
            System.out.println("Excecao " + iOE);
        }
    } 
    
    /**
     * Abre uma janela do explorador de arquivos no endereco informado.
     * @param endereco
     */
    public static void abreDiretorio(String endereco){
        
        try {
            File diretorio = new File(endereco);

            // Verifica se a plataforma e suportada
            if (Desktop.isDesktopSupported()) {

                Desktop desktop = Desktop.getDesktop();

                if (diretorio.exists()) {
                    desktop.open(diretorio);
                } else {
                    System.out.println("Ao abrir o explorador: Diretorio nao existente " + endereco);
                }
            } else {
                System.out.println("Ao abrir o explorador: Plataforma nao suportada.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
      }
    
}
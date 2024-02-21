package br.ufsc.core.entity;


import br.ufsc.core.excel.Revisor;


public class Publicacao {
    // No formato MM/AAAA
    private String data;

    public Publicacao(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String pegaMesData(){
    /* Pega o atributo data no formato MM/AAAA e retorna o mês apenas
        com as três primeiras letras e maiusculo. Ex: "03/2023" -> "MAR"
        */
        String mes;
        
        if(!this.getData().equals("Nao Encontrado")){
            mes = this.getData();
            mes = mes.split("/")[0];
            mes = Revisor.formataMes(mes);
        }else{
            mes = "MM";
        }
        return mes;
    }
    
    public String pegaAnoData(){
    /* Pega o atributo data no formato MM/AAAA e retorna o apenas o 
        Ex. "03/2023" -> "2023".
    */
        String ano;
        if(!this.getData().equals("Nao Encontrado")){
            ano = this.getData();
            ano = ano.split("/")[1];
        }else{
            ano = "AAAA";
        }
        
        return ano;
    }
   
    /* Retorna a data da publicacao no formato MMM_AAAA.
        */
    public String pegaDataFormatada(){
        String data;
        
        data = this.pegaMesData() + "_" + this.pegaAnoData();
        
        return data;
    }
    
}


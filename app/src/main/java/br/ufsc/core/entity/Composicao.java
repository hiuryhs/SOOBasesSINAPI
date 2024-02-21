package br.ufsc.core.entity;


import java.util.TreeMap;


public class Composicao {
    private double codigo;
    private String descricao;
    private String unidade;
    private double custoMoDeso;
    private double custoMatDeso;
    private double custoEqDeso;
    private double custoMoOner;
    private double custoMatOner;
    private double custoEqOner;
    
    private TreeMap<Double, Elemento> filhos;
    
    public Composicao() {
        filhos = new TreeMap<Double, Elemento>();
    }
    
    @Override
    public String toString(){
        String str;
        
        str = "COMPOSICAO\n";
        str += "Código: " + this.getCodigo() + "\n";
        str += "Descrição: " + this.getDescricao() + "\n";
        str += "Unidade: " + this.getUnidade() + "\n";
        str += "Custo Mão de Obra Desonerada: " + this.getCustoMoDeso()+ "\n";
        str += "Custo Mão de Obra Onerada: " + this.getCustoMoOner()+ "\n";
        str += "Custo Material Desonerado: " + this.getCustoMatDeso()+ "\n";
        str += "Custo Material Onerado: " + this.getCustoMatOner()+ "\n";
        str += "Custo Equipamento Desonerado: " + this.getCustoEqDeso()+ "\n";
        str += "Custo Equipamento Onerado: " + this.getCustoEqOner()+ "\n";
        
        return str;
    }
    
    @Override
    public boolean equals(Object o){
        
        boolean igual;
        
        if(o == this){
            igual = true;
        } else if(!(o instanceof Composicao)){
            igual = false;
        } else{
            Composicao meCompare = (Composicao) o;
            igual = Double.compare(this.getCodigo(), meCompare.getCodigo()) == 0;
        }
        
        return igual;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.codigo) ^ (Double.doubleToLongBits(this.codigo) >>> 32));
        return hash;
    }

    public double getCustoMoOner() {
        return custoMoOner;
    }

    public void setCustoMoOner(double custoMoOner) {
        this.custoMoOner = custoMoOner;
    }

    public double getCustoMatOner() {
        return custoMatOner;
    }

    public void setCustoMatOner(double custoMatOner) {
        this.custoMatOner = custoMatOner;
    }

    public double getCustoEqOner() {
        return custoEqOner;
    }

    public void setCustoEqOner(double custoEqOner) {
        this.custoEqOner = custoEqOner;
    }

    public double getCustoMoDeso() {
        return custoMoDeso;
    }

    public void setCustoMoDeso(double custoMoDeso) {
        this.custoMoDeso = custoMoDeso;
    }

    public double getCustoMatDeso() {
        return custoMatDeso;
    }

    public void setCustoMatDeso(double custoMatDeso) {
        this.custoMatDeso = custoMatDeso;
    }

    public double getCustoEqDeso() {
        return custoEqDeso;
    }

    public void setCustoEqDeso(double custoEqDeso) {
        this.custoEqDeso = custoEqDeso;
    }

    public double getCodigo() {
        return codigo;
    }

    public void setCodigo(double codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public TreeMap<Double, Elemento> getFilhos() {
        return filhos;
    }

    public void setFilhos(Elemento filho) {
        this.filhos.put(filho.getCodigo(), filho);
    }
    
}

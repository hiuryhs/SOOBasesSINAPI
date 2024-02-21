package br.ufsc.core.entity;


public class Insumo {
    
    private double codigo;
    private String descricao;
    private String unidade;
    private String origem;
    private double custoDeso;
    private double custoOner;
    private String macroClasse;

    
    public Insumo(){
        
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

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getCustoDeso() {
        return custoDeso;
    }

    public void setCustoDeso(double custoDeso) {
        this.custoDeso = custoDeso;
    }

    public double getCustoOner() {
        return custoOner;
    }

    public void setCustoOner(double custoOner) {
        this.custoOner = custoOner;
    }

    public String getMacroClasse() {
        return macroClasse;
    }

    public void setMacroClasse(String macroClasse) {
        this.macroClasse = macroClasse;
    }
    
}

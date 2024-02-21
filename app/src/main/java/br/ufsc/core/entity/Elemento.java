package br.ufsc.core.entity;


public class Elemento {
    private double paiCodigo;
    private String paiDescricao;
    private String paiUnidade;
    private String tipo;
    private double codigo;
    private String origem;
    private double coeficiente;


    public Elemento() {
    }
    
    @Override
    public String toString(){
        
        String str;
        
        str = "Elemento\n";
        str += "Código Pai: " + this.getPaiCodigo() + "\n";
        str += "Descrição Pai: " + this.getPaiDescricao()+ "\n";
        str += "Unidade Pai: " + this.getPaiUnidade() + "\n";
        str += "Tipo: " + this.getTipo() + "\n";
        str += "Código: " + this.getCodigo() + "\n";
        str += "Origem: " + this.getOrigem() + "\n";
        str += "Coeficiente: " + this.getCoeficiente() + "\n";
        
        return str;
    }

    public double getPaiCodigo() {
        return paiCodigo;
    }

    public void setPaiCodigo(double pai) {
        this.paiCodigo = pai;
    }

    public String getPaiDescricao() {
        return paiDescricao;
    }

    public void setPaiDescricao(String paiDescricao) {
        this.paiDescricao = paiDescricao;
    }

    public String getPaiUnidade() {
        return paiUnidade;
    }

    public void setPaiUnidade(String paiUnidade) {
        this.paiUnidade = paiUnidade;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCodigo() {
        return codigo;
    }

    public void setCodigo(double codigo) {
        this.codigo = codigo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public double getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(double coeficiente) {
        this.coeficiente = coeficiente;
    }
    
}

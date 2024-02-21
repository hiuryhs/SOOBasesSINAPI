package br.ufsc.view;

import br.ufsc.core.excel.ColetorComposicao;
import br.ufsc.core.excel.ColetorInsumo;
import br.ufsc.core.excel.EscritorComposicao;
import br.ufsc.core.excel.EscritorHoras;
import br.ufsc.core.excel.EscritorInsumo;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class TelaPrincipalController implements Initializable {

    private ColetorComposicao coletorComp;
    private ColetorInsumo coletorInsu;
    
    // Armazenam os enderecos das planilhas para as bases de precos SOO-SINAPI - PRECOS
    private String caminhoPSSAD;
    private String caminhoPSID;
    private String caminhoPSSAO;
    private String caminhoPSIO;
    private String caminhoPSFI;
    // Armazenam os enderecos das planilhas para as bases de precos SOO-SINAPI - HORAS
    private String caminhoPSSA;
    private String caminhoPSSADH;
    private String caminhoPSSAOH;
    // Armazena os enderecos dos destino
    private String destinoPrecos;
    private String destinoHoras;
    
    private int codColetaComp;
    private int codColetaInsu;
    
    Stage telaProgresso;
    
    @FXML
    private Button botaoSelPSSAD;
    @FXML
    private TextField campoPSSAD;
    @FXML
    private Button botaoSelPSID;
    @FXML
    private TextField campoPSID;
    @FXML
    private Button botaoSelPSSAO;
    @FXML
    private TextField campoPSSAO;
    @FXML
    private Button botaoSelPSIO;
    @FXML
    private TextField campoPSIO;
    @FXML
    private Button botaoSelPSFI;
    @FXML
    private TextField campoPSFI;
    @FXML
    private Button botaoExecutaBasesPrecos;
    @FXML
    private TextField campoDestinoPrecos;
    @FXML
    private Button botaoExecutaBasesHoras;
    @FXML
    private TextField campoDestinoHoras;
    @FXML
    private Button botaoSelDestHoras;
    @FXML
    private Button botaoSelDestPrecos;
    @FXML
    private Button botaoSelPSSADH;
    @FXML
    private TextField campoPSSADH;
    @FXML
    private Button botaoSelPSSAOH;
    @FXML
    private TextField campoPSSAOH;

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    /**
     * Instancia o coletor de composicoes caso esse ainda nao tenha sido criado.
     */
    private void criaColetorComp(String enderecoDeso, String enderecoOner){
        if(this.coletorComp == null){
            this.coletorComp = new ColetorComposicao(enderecoDeso, enderecoOner);
        }
    }
    
    /**
     * Instancia o coletor de insumos caso esse ainda nao tenha sido criado.
     */
    private void criaColetorInsu(String enderecoDeso, String enderecoOner, String enderecoFam){
        if(this.coletorInsu == null){
            this.coletorInsu = new ColetorInsumo(enderecoDeso, enderecoOner, enderecoFam);
        }
    }
    
    /**
     * Se a coleta das composicoes ainda nao foi realizada na base de dados,
     * executa-a.
     */
    private int coletaComposicoes(){
        
        int codigoRetorno;
        
        if (this.coletorComp.getComposicoes() == null){
            codigoRetorno = this.coletorComp.buscaComposicoes();
        }else{ // Caso a coleta ja foi realizada, retorna o codigo armazenado
            codigoRetorno = this.codColetaComp;
        }
        
        return codigoRetorno;
    }

    /**
     * Se a coleta dos insumos ainda nao foi realizada na base de dados,
     * executa-a.
     */
    private int coletaInsumos(){
        
        int codigoRetorno;
        
        if(this.coletorInsu.getInsumos() == null){
            codigoRetorno = this.coletorInsu.buscaInsumosPrecos();
        }else{ // Caso a coleta ja foi realizada, retorna o codigo armazenado
            codigoRetorno = this.codColetaInsu;
        }
        
        return codigoRetorno;
    }
    
    /**
     * Cria uma janela de alerta (Alert - Dialog) com um botão de confirmação com as informações
     * presentes nas Strings de parametro. 
     * @param titulo
     * @param cabeca
     * @param texto
     * @param txtBotao
     */
    private void criaJanelaAlerta(String titulo, String cabeca, String texto,
            String txtBotao, Alert.AlertType tipoAlerta){
        
        // Instancia um objeto do tipo Alerta (que estende a classe Dialog)
        Alert janelaAlerta = new Alert(tipoAlerta);
        janelaAlerta.setTitle(titulo);
        janelaAlerta.setHeaderText(cabeca);
        janelaAlerta.setContentText(texto);

        // Cria um botao de confirmacao
        ButtonType botaoConfirma = new ButtonType(txtBotao);

        // Adiciona o botao de confirmacao a janela de alerta
        janelaAlerta.getButtonTypes().setAll(botaoConfirma);

        // Mostra a janela e pausa a execucao do codigo ate o usuario clicar no
        // botao
        janelaAlerta.show();
       
    }
    
     /**
     * Cria uma janela de alerta (Alert - Dialog) com dois botões para confirmar, 
     * e retorna o valor da confirmação.
     * 
     * @param titulo
     * @param cabeca
     * @param texto
     * @param txtBotao1
     */
    private boolean criaJanelaEscolha(String titulo, String cabeca, String texto,
            String txtBotao1, String txtBotao2, Alert.AlertType tipoAlerta){
        
        boolean confirma;
        // Instancia um objeto do tipo Alerta (que estende a classe Dialog)
        Alert janelaAlerta = new Alert(tipoAlerta);
        janelaAlerta.setTitle(titulo);
        janelaAlerta.setHeaderText(cabeca);
        janelaAlerta.setContentText(texto);

        // Cria um botao de confirmacao
        ButtonType botaoSim = new ButtonType(txtBotao1);
        ButtonType botaoNao = new ButtonType(txtBotao2);

        // Adiciona o botao de confirmacao a janela de alerta
        janelaAlerta.getButtonTypes().setAll(botaoSim, botaoNao);
        
        // Configura o resultado do clique nos botoes e mostra a janela
        Optional<ButtonType> result = janelaAlerta.showAndWait();
        confirma = result.isPresent() && result.get() == botaoSim;
        
        return confirma;
    }
    
    /**
     * Cria e abre uma janela (Stage) com um um indicador de progresso indeterminado.
     * 
     * @param titulo
     * @param texto 
     */
    private void criaTelaProgresso(String titulo, String texto) {
            // Cia o estagio para a tela de progresso
            this.telaProgresso = new Stage();
            this.telaProgresso.setTitle(titulo);
            this.telaProgresso.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icons/ufsc_fundo_claro.png")));

            // Carrega o arquivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/telaProgresso.fxml"));
            Parent raiz;
            try {
                raiz = loader.load();
                Scene scene = new Scene(raiz);
                
                // Pega o Controlador e muda o texto conforme o parametro texto
                TelaProgressoController controlaProg =  loader.getController();
                controlaProg.setLabelTexto1(texto);
                
                this.telaProgresso.setScene(scene);
                telaProgresso.initModality(Modality.APPLICATION_MODAL);
                this.telaProgresso.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    private void fechaTelaProgresso(){
        if(this.telaProgresso != null){
            this.telaProgresso.close();
        }
    }
    
    /**
     * Abre uma janela FileChooser para sele��o de um arquivo .xlsx.
     * Retorna o arquivo tipo File.
     * 
     * @param titulo
     * @return 
     */
    private File criaTelaSelArquivo(String titulo){
        //Cria uma instancia da classe para abrir o seletor
        FileChooser selecionaArquivo = new FileChooser();
        selecionaArquivo.setTitle(titulo);

        // Abre o selector de arquivo no diretorio do programa
        File diretorioInicial = new File(System.getProperty("user.dir"));
        selecionaArquivo.setInitialDirectory(diretorioInicial);

        // Filtra a extensao de arquivo para xlsx
        FileChooser.ExtensionFilter filtroExtensao = new FileChooser.ExtensionFilter("Arquivo Excel (*.xlsx)", "*.xlsx");
        selecionaArquivo.getExtensionFilters().add(filtroExtensao);

        // Abre a tela do seletor
        File arquivo = selecionaArquivo.showOpenDialog(new Stage());
        
        return arquivo;
    }
    
    /**
     * Abre uma janela DirectoryChooser para selecao de um diretorio.
     * @param titulo
     * @return 
     */
    private File criaTelaSelDiretorio(String titulo){
        DirectoryChooser selecionaDir = new DirectoryChooser();
        selecionaDir.setTitle(titulo);
        
        File diretorioInicial = new File(System.getProperty("user.dir"));
        selecionaDir.setInitialDirectory(diretorioInicial);
        
        File diretorioSel = selecionaDir.showDialog(new Stage());
        
        return diretorioSel;
    }
    
    /** 1 - Salva um endereco a partir de um objeto File no atributo informado.
     * 2 - Atualiza o campo da interface para mostrar o nome (se completo== falso)
     * ou o endereco completo (completo==true) do arquivo selecionado;
     * 3 - Caso File arquivo seja nulo, imprime uma mensagem com os dados.
     * 
     * @param arquivo
     * @param caminho
     * @param campo 
     */
    private void selecionaCaminho(String tituloTela, String nomeAtributo, TextField campoTexto, boolean diretorio){
        
        File arquivo;
        
        if(diretorio){
            arquivo = this.criaTelaSelDiretorio(tituloTela);
        }else{
            arquivo = this.criaTelaSelArquivo(tituloTela);
        }
        
        if (arquivo != null) {
            try {
                Field atributo = getClass().getDeclaredField(nomeAtributo);
                atributo.setAccessible(true);
                atributo.set(this, arquivo.getAbsolutePath());
                if(diretorio){
                    campoTexto.setText(arquivo.getAbsolutePath());
                }else{
                    campoTexto.setText(arquivo.getName());
                }
            
            } catch (NoSuchFieldException | IllegalAccessException e) {
                StringBuilder msg = new StringBuilder();
                msg.append("Erro ao acessar o atributo da Tela Principal: ");
                msg.append(nomeAtributo);
                msg.append(".");
                System.out.println(msg);
                e.printStackTrace();
            }
        } else {
            StringBuilder msg = new StringBuilder();
            msg.append("Arquivo referente ao ");
            msg.append(nomeAtributo);
            msg.append(" e nulo.");
            System.out.println(msg);
        }
    }
    
    /**
     * Utilizando os dados coletados da planilha base pelo coletor, realiza as 
     * acoes necessarias para abrir as composicoes e salvar uma planilha .xlsx
     * com o resultado OU apresentar uma tela de erro, caso uma ou mais composicoes
     * nao constem na base.
     */    
    private void executaAbertura(){

            // Recupera o caminho de onde o programa esta sendo executado
            Path caminhoAtual = Paths.get("").toAbsolutePath();
            // Converte o caminho para String, que sera utilizada como local
            // de salvamento da pasta Excel
            String enderecoSalva = caminhoAtual.toString() + "\\";
            // Cria a pasta e salva no mesmo diretorio que o programa
     
    }
 
    /**
     * Cria uma tarefa a partir de um bloco de codigo.
     * @param tarefa
     * @return 
     */
    private Task<Void> criaTarefa(Runnable tarefa){
        // Create a background Task
            Task<Void> tarefaFundo = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    tarefa.run();
                    return null;
                }
            };

            tarefaFundo.setOnFailed(wse -> {
                wse.getSource().getException().printStackTrace();
            });
            
            return tarefaFundo;
    }
    
    
    /**
     * Verifica se as planilhas para formacao das bases de precos SINAPI ja
     * foram selecionadas.
     * 
     * Retorna true caso sim.
     * @return 
     */
    private boolean planilhasSelecionadas(){
        
        boolean selecionadas = false;
        
        if(this.caminhoPSSAD != null &&
                this.caminhoPSID != null &&
                this.caminhoPSSAO != null &&
                this.caminhoPSIO != null &&
                this.caminhoPSFI != null
                ){
            selecionadas = true;
        }
        
        return selecionadas;
    }
    
    private void criaBaseComp(){
        
        Task<Void> criarBaseComp = this.criaTarefa(()->{
            EscritorComposicao escritorComp = new EscritorComposicao();
            
            escritorComp.criaBaseComp(this.coletorComp.getComposicoes(), 
                    this.coletorComp.getDadosPublicacao().pegaDataFormatada(),
                    this.destinoPrecos + "\\");
        });
        criarBaseComp.setOnSucceeded(wse -> this.fechaTelaProgresso());
        
        new Thread(criarBaseComp).start();
        this.criaTelaProgresso("Processando", "Criando base de Composicoes SOO-SINAPI.");
    }
    
    private void criaBaseInsu(){
        
        Task<Void> criarBaseInsu = this.criaTarefa(()->{
            EscritorInsumo escritorInsu = new EscritorInsumo();
            escritorInsu.criaBaseInsu(this.coletorInsu.getInsumos(),
                    this.coletorInsu.getDadosPublicacao().pegaDataFormatada(),
                    this.destinoPrecos + "\\");
        });
        criarBaseInsu.setOnSucceeded(wse -> this.fechaTelaProgresso());
        
        new Thread(criarBaseInsu).start();
        this.criaTelaProgresso("Processando", "Criando base de Insumos SOO-SINAPI.");
    }
    
    private void criaBaseHoras(){
        
        Task<Void> criarBaseHoras = this.criaTarefa(()->{
            EscritorHoras escritorHoras = new EscritorHoras();
            escritorHoras.criaBaseHoras(this.coletorComp.getComposicoes(),
                    this.coletorComp.getDadosPublicacao().pegaDataFormatada(),
                    this.destinoPrecos + "\\");
        });
        criarBaseHoras.setOnSucceeded(wse -> this.fechaTelaProgresso());
        
        new Thread(criarBaseHoras).start();
        this.criaTelaProgresso("Processando", "Criando base de Horas SOO-SINAPI.");
    }

    @FXML
    private void botaoSelPSSADClic(ActionEvent event) {
        this.selecionaCaminho("Planilha SINAPI de Servicos Analitica Desonerada.", 
                "caminhoPSSAD", 
                campoPSSAD, 
                false);
        
        // Caso a planilha SINPI para formacao de horas nao tenha sido selecionada,
        // atualiza os campos tambem:
        if(this.caminhoPSSADH == null && this.caminhoPSSAD != null){
            this.caminhoPSSADH = this.caminhoPSSAD;
            this.campoPSSADH.setText(
            this.campoPSSAD.getText());
        }
        // Elimina o coletor existente
        this.coletorComp = null;
    }

    @FXML
    private void botaoSelPSIDClic(ActionEvent event) {
        this.selecionaCaminho("Planilha SINAPI de Insumos Desonerada.", 
                "caminhoPSID", 
                campoPSID, 
                false);
        
        // Elimina o coletor existente
        this.coletorInsu = null;
    }

    @FXML
    private void botaoSelPSSAOClic(ActionEvent event) {
        this.selecionaCaminho("Planilha SINAPI de Servicos Analitica Onerada.", 
                "caminhoPSSAO", 
                campoPSSAO, 
                false);
        
        // Caso a planilha SINPI para formacao de horas nao tenha sido selecionada,
        // atualiza os campos tambem:
        if(this.caminhoPSSAOH == null && this.caminhoPSSAO != null){
            this.caminhoPSSAOH = this.caminhoPSSAO;
            this.campoPSSAOH.setText(
            this.campoPSSAO.getText());
        }
        
        // Elimina o coletor existente
        this.coletorComp = null;
    }

    @FXML
    private void botaoSelPSIOClic(ActionEvent event) {
        this.selecionaCaminho("Planilha SINAPI de Insumos Onerada.",  
                "caminhoPSIO", 
                campoPSIO, 
                false);
        
        // Elimina o coletor existente
        this.coletorInsu = null;
    }

    @FXML
    private void botaoSelPSFIClic(ActionEvent event) {
        this.selecionaCaminho("Planilha SINAPI de Familia de Insumos.", 
                "caminhoPSFI", 
                campoPSFI, 
                false);
        
        // Elimina o coletor existente
        this.coletorInsu = null;
    }

    @FXML
    private void botaoSelPSSADHClic(ActionEvent event) {
        this.selecionaCaminho("Planilha SINAPI de Servicos Analitica Desonerada.",
        "caminhoPSSADH", 
        campoPSSADH, 
        false);
        
        // Caso a planilha SINPI para formacao de precos nao tenha sido selecionada,
        // atualiza os campos tambem:
        if(this.caminhoPSSAD == null && this.caminhoPSSADH != null){
            this.caminhoPSSAD = this.caminhoPSSADH;
            this.campoPSSAD.setText(
            this.campoPSSADH.getText());
        }
        // Elimina o coletor existente
        this.coletorComp = null;
    }

    @FXML
    private void botaoSelPSSAOHClic(ActionEvent event) {
        this.selecionaCaminho("Planilha SINAPI de Servicos Analitica Onerada.",
        "caminhoPSSAOH", 
        campoPSSAOH, 
        false);
        
        // Caso a planilha SINPI para formacao de precos nao tenha sido selecionada,
        // atualiza os campos tambem:
        if(this.caminhoPSSAO == null && this.caminhoPSSAOH != null){
            this.caminhoPSSAO = this.caminhoPSSAOH;
            this.campoPSSAO.setText(
            this.campoPSSAOH.getText());
        }
        // Elimina o coletor existente
        this.coletorComp = null;
    }
    
    /**
     * 1 - Salva o endereco do diretorio destino selecionado.
     * 2 - Atualiza o campo de texto do diretorio destino selecionado.
     * *3 - Caso o destino da base de Horas nao tenha sido selecionado ainda, o atualiza.
     * @param event 
     */
    @FXML
    private void botaoSelDesPrecostClic(ActionEvent event) {
        this.selecionaCaminho("Selecione o destino", 
                "destinoPrecos", 
                campoDestinoPrecos, 
                true);
        
        if(this.destinoHoras == null && this.destinoPrecos != null){
            this.destinoHoras = this.destinoPrecos;
            this.campoDestinoHoras.setText(
            this.campoDestinoPrecos.getText());
        }
    }
    
     /**
     * 1 - Salva o endereco do diretorio destino selecionado.
     * 2 - Atualiza o campo de texto do diretorio destino selecionado.
     * *3 - Caso o destino da base de Precos nao tenha sido selecionado ainda, o atualiza.
     * @param event 
     */   
    @FXML
    private void botaoSelDestHorasClic(ActionEvent event) {
        this.selecionaCaminho("Selecione o destino", 
                "destinoHoras", 
                campoDestinoHoras, 
                true);
        
        if(this.destinoPrecos == null && this.destinoHoras != null){
            this.destinoPrecos = this.destinoHoras;
            this.campoDestinoPrecos.setText(
            this.campoDestinoHoras.getText());
        }
    }


    @FXML
    private void botaoExecBasesDadosClic(ActionEvent event) {
        if(this.planilhasSelecionadas()){
            if(this.destinoPrecos != null){
            // Processa as planilhas da base SINAPI e atua conforme os resultados
            // das coletas
            Task<Void> coletaComp = this.criaTarefa(() -> {
                this.criaColetorComp(this.caminhoPSSAD, this.caminhoPSSAO);
                this.codColetaComp = this.coletaComposicoes();
            });
            coletaComp.setOnSucceeded(wse -> {
                this.telaProgresso.close();
            });
            Task<Void> coletaInsu = this.criaTarefa(() -> {
                int codigo;
                this.criaColetorInsu(caminhoPSID, caminhoPSIO, caminhoPSFI);
                codigo = this.coletaInsumos();
                this.codColetaInsu = codigo;
            });
            coletaInsu.setOnSucceeded(wse -> {
                this.telaProgresso.close();
            });
            
            new Thread(coletaComp).start();
            this.criaTelaProgresso("Processando", "Processando planilhas de serviços, aguarde.");
            new Thread(coletaInsu).start();
            this.criaTelaProgresso("Processando", "Processando planilhas de insumos, aguarde.");
            
            if(this.codColetaComp == 1 && this.codColetaInsu == 1){
                this.criaBaseComp();
                this.criaBaseInsu();
                this.criaJanelaAlerta("Sucesso", 
                        "Bases criadas com sucesso!", 
                        "Aperte \"confirmar\" para retornar.", 
                        "confirmar",
                        Alert.AlertType.INFORMATION);
            }
            else if(this.codColetaComp == 1 && this.codColetaInsu == 0){
                this.criaBaseComp();
                this.criaJanelaAlerta("ALERTA!", 
                        "Datas de preços - Planilhas base de Insumos", 
                        "As datas dos preços são diferentes, a base SOO-SINAPI de INSUMOS NÃO FOI GERADA, apenas a de composições.", 
                        "confirmar",
                        Alert.AlertType.WARNING);                
            }
            else if(this.codColetaComp == 1 && this.codColetaInsu == -1){
                if(this.criaJanelaEscolha("ALERTA!",
                        "As datas das publicacoes de INSUMOS nao puderam ser verificadas. Isso pode ter ocorrido porque:\n\n- A CEF modficou o formato do cabeçalho das publicacoes.\n- O cabecalho foi editado por um usuario.\n- A(s) planilha(s) selecionada(s) nao é(sao) da publicacao esperada.",
                        "Deseja continuar?",
                        "Sim",
                        "Nao",
                        Alert.AlertType.WARNING
                        )){
                    this.criaBaseComp();
                    this.criaBaseInsu();
                        this.criaJanelaAlerta("Sucesso", 
                        "Bases criadas com sucesso!", 
                        "Aperte \"confirmar\" para retornar.", 
                        "confirmar",
                        Alert.AlertType.WARNING);
                }else{
                    this.criaBaseComp();
                        this.criaJanelaAlerta("Sucesso Parcial", 
                        "Apenas a base de COMPOSIÇÕES foi criada com sucesso.", 
                        "Aperte \"confirmar\" para retornar.", 
                        "confirmar",
                        Alert.AlertType.WARNING);
                }
            }
            else if(this.codColetaComp == 0 && this.codColetaInsu == 1){
                this.criaBaseInsu();
                this.criaJanelaAlerta("Sucesso Parcial", 
                        "Datas de preços - Planilhas base de Composições", 
                        "As datas dos preços são diferentes, a base SOO-SINAPI de COMPOSIÇÕES NÃO FOI GERADA, apenas a de insumos.", 
                        "confirmar",
                        Alert.AlertType.WARNING);         
            }
            else if(this.codColetaComp == 0 && this.codColetaInsu == 0){
                this.criaJanelaAlerta("ALERTA!", 
                        "Planilhas base SINAPI", 
                        "As datas dos preços das planilhas base SINAPI são diferentes, as bases SOO-SINAPI não foram geradas.", 
                        "confirmar",
                        Alert.AlertType.ERROR);         
            }
            else if(this.codColetaComp == 0 && this.codColetaInsu == -1){
                        if(this.criaJanelaEscolha("ALERTA!",
                        "As datas das publicacoes de INSUMOS nao puderam ser verificadas. Isso pode ter ocorrido porque:\n\n- A CEF modficou o formato do cabeçalho das publicacoes.\n- O cabecalho foi editado por um usuario.\n- A(s) planilha(s) selecionada(s) nao é(sao) da publicacao esperada.",
                        "Deseja continuar?",
                        "Sim",
                        "Nao",
                        Alert.AlertType.WARNING
                        )){
                    this.criaBaseInsu();
                        this.criaJanelaAlerta("Sucesso Parcial", 
                        "Datas de preços - Planilhas base de Insumos", 
                        "As datas dos preços são diferentes, a base SOO-SINAPI de COMPOSIÇÕES NÃO FOI GERADA, apenas a de insumos.", 
                        "confirmar",
                        Alert.AlertType.INFORMATION);
                }else{
                    this.criaJanelaAlerta("ALERTA!", 
                        "Datas de preços - Planilhas base de Composições / Inconsistência de dados - Planilhas de Insumos", 
                        "As datas dos preços das planilhas de COMPOSIÇÕES são diferentes. Nenhuma base SOO-SINAPI foi gerada.", 
                        "confirmar",
                        Alert.AlertType.ERROR);         
                }
            }
            else if(this.codColetaComp == -1 && this.codColetaInsu == -1){
                if(this.criaJanelaEscolha("ALERTA!",
                        "As datas das publicacoes de INSUMOS nao puderam ser verificadas. Isso pode ter ocorrido porque:\n\n- A CEF modficou o formato do cabeçalho das publicacoes.\n- O cabecalho foi editado por um usuario.\n- A(s) planilha(s) selecionada(s) nao é(sao) da publicacao esperada.",
                        "Deseja continuar?",
                        "Sim",
                        "Nao",
                        Alert.AlertType.WARNING
                        )){
                    this.criaBaseInsu();
                    this.criaJanelaAlerta("Sucesso Parcial", 
                        "Sucesso parcial - base Insumos.", 
                        "Base SOO-SINAPI de INSUMOS criada com sucesso.", 
                        "confirmar",
                        Alert.AlertType.INFORMATION);
                }
                if(this.criaJanelaEscolha("ALERTA!",
                        "As datas das publicacoes de COMPOSIÇÕES nao puderam ser verificadas. Isso pode ter ocorrido porque:\n\n- A CEF modficou o formato do cabeçalho das publicacoes.\n- O cabecalho foi editado por um usuario.\n- A(s) planilha(s) selecionada(s) nao é(sao) da publicacao esperada.",
                        "Deseja continuar?",
                        "Sim",
                        "Nao",
                        Alert.AlertType.WARNING
                        )){
                    this.criaBaseComp();
                        this.criaJanelaAlerta("Sucesso Parcial", 
                        "Sucesso parcial - base Composições.", 
                        "Base SOO-SINAPI de COMPOSIÇÕES criada com sucesso.", 
                        "confirmar",
                        Alert.AlertType.INFORMATION);
                }
            }
            else if(this.codColetaComp == -1 && this.codColetaInsu == 1){
                if(this.criaJanelaEscolha("ALERTA!",
                        "As datas das publicacoes de COMPOSIÇÕES nao puderam ser verificadas. Isso pode ter ocorrido porque:\n\n- A CEF modficou o formato do cabeçalho das publicacoes.\n- O cabecalho foi editado por um usuario.\n- A(s) planilha(s) selecionada(s) nao é(sao) da publicacao esperada.",
                        "Deseja continuar?",
                        "Sim",
                        "Nao",
                        Alert.AlertType.WARNING
                        )){
                    this.criaBaseComp();
                    this.criaBaseInsu();
                        this.criaJanelaAlerta("Sucesso", 
                        "Bases criadas com sucesso!", 
                        "Aperte \"confirmar\" para retornar.", 
                        "confirmar",
                        Alert.AlertType.INFORMATION);
                }else{
                    this.criaBaseInsu();
                        this.criaJanelaAlerta("Sucesso Parcial", 
                        "Apenas a base de INSUMOS foi criada com sucesso.", 
                        "Aperte \"confirmar\" para retornar.", 
                        "confirmar",
                        Alert.AlertType.WARNING);
                }
            }
            else if(this.codColetaComp == -1 && this.codColetaInsu == 0){
                 if(this.criaJanelaEscolha("ALERTA!",
                        "As datas das publicacoes de COMPOSIÇÕES nao puderam ser verificadas. Isso pode ter ocorrido porque:\n\n- A CEF modficou o formato do cabeçalho das publicacoes.\n- O cabecalho foi editado por um usuario.\n- A(s) planilha(s) selecionada(s) nao é(sao) da publicacao esperada.",
                        "Deseja continuar?",
                        "Sim",
                        "Nao",
                        Alert.AlertType.WARNING
                        )){
                    this.criaBaseComp();
                        this.criaJanelaAlerta("Sucesso Parcial", 
                        "Datas de preços - Planilhas base de Insumos", 
                        "As datas dos preços são diferentes, a base SOO-SINAPI de INSUMOS NÃO FOI GERADA, apenas a de composições.", 
                        "confirmar",
                        Alert.AlertType.WARNING);
                }else{
                    this.criaJanelaAlerta("ALERTA!", 
                        "Datas de preços - Planilhas base de Insumos / Inconsistência de dados - Planilhas de Composições", 
                        "As datas dos preços das planilhas de INSUMOS são diferentes. Nenhuma base SOO-SINAPI foi gerada.", 
                        "confirmar",
                        Alert.AlertType.ERROR);         
                }
            }
          
            }else{
                this.criaJanelaAlerta("Pasta Destino", 
                    "Local de Destino não selecionado.", 
                    "Selecione o diretório para salvamento das bases geradas.", 
                    "confirmar",
                    Alert.AlertType.WARNING);
            }
        }else{
            this.criaJanelaAlerta("Selecionar Planilhas", 
                    "Planilhas SINAPI não selecionadas.", 
                    "Selecione todas as planilhas SINAPI necessárias.", 
                    "confirmar",
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void botaoExecBasesHorasClic(ActionEvent event) {
        if(this.caminhoPSSADH != null & this.caminhoPSSAOH != null){
            if(this.destinoHoras != null){
                    Task<Void> coletaComp = this.criaTarefa(() -> {
                    this.criaColetorComp(this.caminhoPSSADH, this.caminhoPSSAOH);
                    this.codColetaComp = this.coletaComposicoes();
                });
                coletaComp.setOnSucceeded(wse -> {
                    this.telaProgresso.close();
                });

                new Thread(coletaComp).start();
                this.criaTelaProgresso("Processando", "Processando planilhas de serviços, aguarde.");

                switch (this.codColetaComp) {
                    case 1:
                        this.criaBaseHoras();
                        this.criaJanelaAlerta("Sucesso",
                                "Base SOO - HORAS criada com sucesso!",
                                "Aperte \"confirmar\" para retornar.",
                                "confirmar",
                                Alert.AlertType.INFORMATION);
                        break;

                    case -1:
                        if(this.criaJanelaEscolha("ALERTA!",
                                "As datas das publicacoes de COMPOSIÇÕES nao puderam ser verificadas. Isso pode ter ocorrido porque:\n\n- A CEF modficou o formato do cabeçalho das publicacoes.\n- O cabecalho foi editado por um usuario.\n- A(s) planilha(s) selecionada(s) nao é(sao) da publicacao esperada.",
                                "Deseja continuar?",
                                "Sim",
                                "Nao",
                                Alert.AlertType.WARNING
                        )){
                            this.criaBaseHoras();
                            this.criaJanelaAlerta("Sucesso",
                                    "Base SOO - HORAS criada com sucesso!",
                                    "Aperte \"confirmar\" para retornar.",
                                    "confirmar",
                                    Alert.AlertType.INFORMATION);
                        }   break;

                    case 0:
                        this.criaJanelaAlerta("ALERTA!",
                                "Planilhas base SINAPI",
                                "As datas dos preços das planilhas base SINAPI são diferentes, a base SOO - HORAS não foi gerada.",      
                                "confirmar",
                                Alert.AlertType.ERROR);
                        break;
                }
            }else{
                this.criaJanelaAlerta("Pasta Destino", 
                        "Local de Destino não selecionado.", 
                        "Selecione o diretório para salvamento das bases geradas.", 
                        "confirmar",
                        Alert.AlertType.WARNING);
            }
        }else{
            this.criaJanelaAlerta("Selecionar Planilhas", 
                    "Planilhas SINAPI não selecionadas.", 
                    "Selecione todas as planilhas SINAPI necessárias.", 
                    "confirmar",
                    Alert.AlertType.WARNING);
        }
    }
}


package interfacegrafica;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sistemagestao.Produtos;
import sistemagestao.ProdutosDao;

public class Controlador implements Initializable {
	
	private ProdutosDao produtodao = new ProdutosDao();

	private Long identificacao;

	private ObservableList<Produtos> obsProdutos;

	@FXML
	private TextField campoNome;

	@FXML
	private TextField campoQuantidade;

	@FXML
	private TextField campoPreco;

	@FXML
	private Button btnLimpar;

	@FXML
	private Button btnInserir;

	@FXML
	private Button btnAlterar;

	@FXML
	private Button btnDeletar;

	@FXML
	private TableView<Produtos> tabelaDados;

	@FXML
	private TableColumn<Produtos, Integer> colunaOrdem;

	@FXML
	private TableColumn<Produtos, String> colunaNome;

	@FXML
	private TableColumn<Produtos, Integer> colunaQuantidade;

	@FXML
	private TableColumn<Produtos, Double> colunaPreco;

	@FXML
	public void removerDados() {
		// Remove um produto no banco de dados
    		if (identificacao != null) {
			if (produtodao.remover(identificacao)) {
    				atualizarDados();
		    		Alerta.mostrarAlerta(AlertType.CONFIRMATION, "Sucesso", null, "Remoção realizada com sucesso!");
	    	
    			} else {
    				Alerta.mostrarAlerta(AlertType.ERROR, "Falha", null, "Não foi possível realizar a remoção. Consulte o arquivo de log.");
    			}
    		
    			limparSelecao();
		} else {	
			Alerta.mostrarAlerta(AlertType.WARNING, "Aviso", null, "Por favor, selecione um item na tabela antes de tentar removê-lo.");
		}	
    	}
    
	@FXML
	public void alterarDados() {
    		// Trata e altera um produto no banco de dados
    		if (identificacao != null) {
    		
    			// Tratando o campo nome
	    		String nome = campoNome.getText();	    	
	    		if (nome.isEmpty()) {
	    			Alerta.mostrarAlerta(AlertType.INFORMATION, "Nome inválido", null, "Por favor, preencha o campo nome. O campo não pode estar vazio.");
	    			return;
	    		}
	    	
	    		nome = nome.trim();
	    	
	    		// Tratando o campo quantidade
	    		String quantidadeStr = campoQuantidade.getText();
	    		if (quantidadeStr.isEmpty()) {
	    			Alerta.mostrarAlerta(AlertType.INFORMATION, "Quantidade inválida", null, "Por favor, preencha o campo quantidade. O campo não pode estar vazio.");
	    			return;
	    		}
	    	
	    		quantidadeStr = quantidadeStr.trim();
	    	
	    		Integer quantidade = null;
	    		try {
	    			quantidade = Integer.parseInt(quantidadeStr);
	    		} catch (NumberFormatException exceptionNumberFormat) {
	    			Alerta.mostrarAlerta(AlertType.WARNING, "Aviso", "Formato inválido", "O valor inserido no campo quantidade é inválido. Por favor, insira um valor válido, como '5' ou '8'.");
	    			return;
	    		}

	    		// Tratando o campo preço
	    		String precoStr = campoPreco.getText();
	    		if (precoStr.isEmpty()) {
	    			Alerta.mostrarAlerta(AlertType.INFORMATION, "Preço inválido", null, "Por favor, preencha o campo preço. O campo não pode estar vazio.");
	    			return;
	    		}
	    	
	    		precoStr = precoStr.replaceAll("\\s+", "");
	    	
	    		if (precoStr.contains(",")) {
	    			precoStr = precoStr.replace(",", ".");
	    		}
	    	
	    		Double preco = null;
	    		try {
	    			preco = Double.parseDouble(precoStr);
	    	
	    		} catch (NumberFormatException exceptionNumberFormat) {
	    			Alerta.mostrarAlerta(AlertType.WARNING, "Aviso", "Formato inválido", "O valor inserido no campo preço é inválido. Por favor, insira um valor válido, como '5,60' ou '8,50'.");
	    			return;
	    		}

	    		// Busca o produto pelo id e realiza a alteração
	    		Produtos produtoEncontrado = produtodao.consultaPorId(identificacao);

    			produtoEncontrado.setNome(nome);
    			produtoEncontrado.setQuantidade(quantidade);
    			produtoEncontrado.setPreco(preco);
    		
    			if (produtodao.atualizar(produtoEncontrado)) {
    				atualizarDados();
    	    			Alerta.mostrarAlerta(AlertType.CONFIRMATION, "Sucesso", null, "Alteração realizada com sucesso!");
    			} else {
    				Alerta.mostrarAlerta(AlertType.ERROR, "Falha", null, "Não foi possível realizar a alteração. Consulte o arquivo de log.");
    			}
    		
    			limparSelecao();
    		} else {
    			Alerta.mostrarAlerta(AlertType.WARNING, "Aviso", null, "Por favor, selecione um item na tabela antes de tentar alterá-lo.");
    		}
    	
    	}
    
	@FXML
	public void inserirDados() {	
    		// Trata, cria e salva o produto no banco de dados 

    		// Tratando o campo nome
    		String nome = campoNome.getText();
    		if (nome.isEmpty()) {
    			Alerta.mostrarAlerta(AlertType.INFORMATION, "Nome inválido", null, "Por favor, preencha o campo nome. O campo não pode estar vazio.");
    			return;
    		}
    	
    		nome = nome.trim();
    	
    		// Tratando o campo quantidade
    		String quantidadeStr = campoQuantidade.getText();
    		if (quantidadeStr.isEmpty()) {
    			Alerta.mostrarAlerta(AlertType.INFORMATION, "Quantidade inválida", null, "Por favor, preencha o campo quantidade. O campo não pode estar vazio.");
    			return;
    		}
    	
    		quantidadeStr = quantidadeStr.trim();
    	
    		Integer quantidade = null;
		try {
    			quantidade = Integer.parseInt(quantidadeStr);
    		} catch (NumberFormatException exceptionNumberFormat) {
    			Alerta.mostrarAlerta(AlertType.WARNING, "Aviso", "Formato inválido", "O valor inserido no campo quantidade é inválido. Por favor, insira um valor válido, como '5' ou '8'.");
    			return;
    		}
    	
    		// Tratando o campo preço 
    		String precoStr = campoPreco.getText();
    		if (precoStr.isEmpty()) {
    			Alerta.mostrarAlerta(AlertType.INFORMATION, "Preço inválido", null, "Por favor, preencha o campo preço. O campo não pode estar vazio.");
    			return;
    		}
    	
    		precoStr = precoStr.replaceAll("\\s+", "");
    	
    		if (precoStr.contains(",")) { 
			precoStr = precoStr.replace(",", ".");
    		}
    	
    		Double preco = null;
		try {
    			preco = Double.parseDouble(precoStr);
    	
    		} catch (NumberFormatException exceptionNumberFormat) {
    			Alerta.mostrarAlerta(AlertType.WARNING, "Aviso", "Formato inválido", "O valor inserido no campo preço é inválido. Por favor, insira um valor válido, como '5,60' ou '8.50'.");
    			return;
    		}
    	
    		// Cria e salva o produto
    		Produtos produto = new Produtos();
    		produto.setNome(nome);
    		produto.setQuantidade(quantidade);
    		produto.setPreco(preco);

    		if (produtodao.salvar(produto)) {
    			atualizarDados();
    		
    			identificacao = null;
	    	
	    		// Limpa os campos de texto
	    		campoNome.clear();
	    		campoQuantidade.clear();
	    		campoPreco.clear();
	    	
	    		Alerta.mostrarAlerta(AlertType.CONFIRMATION, "Sucesso!", null, "Inserção realizada com sucesso!");
    		} else {
    			Alerta.mostrarAlerta(AlertType.ERROR, "Falha", null, "Não foi possível realizar a inserção. Consulte o arquivo de log.");
    		}
    	
	}
    
	@FXML
	public void limparSelecao() {
		// Limpa os campos de texto e desmarca a seleção na tabela
		if (identificacao != null) {
			identificacao = null;
			tabelaDados.getSelectionModel().clearSelection();
			campoNome.clear();
			campoQuantidade.clear();
			campoPreco.clear();
		}
	}

	public void criarTabela() {
		// Configura a tabela
		colunaOrdem.setCellValueFactory(new PropertyValueFactory<>("ordem"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

		obsProdutos = FXCollections.observableArrayList();
		tabelaDados.setItems(obsProdutos);
	}
    
	@FXML
	public void clicarTabela() {
    		// Preenche os campos de texto com os dados do item selecionado na tabela
    		Produtos produtoSelecionado = tabelaDados.getSelectionModel().getSelectedItem();
    		if (produtoSelecionado != null) { 
			identificacao = produtoSelecionado.getId();
			campoNome.setText(produtoSelecionado.getNome());
			campoQuantidade.setText(String.valueOf(produtoSelecionado.getQuantidade()));
			campoPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
    		}
    	}
    
	public void atualizarDados() {
		// Atualiza os dados na tabela
		obsProdutos.clear();
		obsProdutos.addAll(produtodao.listar()); 
	}
      
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		criarTabela();
		atualizarDados();
		btnLimpar.setOnAction(event -> limparSelecao());
		btnInserir.setOnAction(event -> inserirDados());
		btnDeletar.setOnAction(event -> removerDados());
		btnAlterar.setOnAction(event -> alterarDados());
		tabelaDados.setOnMouseClicked(event -> clicarTabela());
	}
	
}

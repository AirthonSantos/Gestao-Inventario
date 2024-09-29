package interfacegrafica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sistemagestao.ProvedorEntityManagerFactory;

public class Main extends Application {
	@Override
	public void start(Stage primeiroEstagio) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/interfaceProjeto.fxml"));
		Image icone = new Image("/iconeCrud.png");

		// Cria a cena e realiza a configuração
		primeiroEstagio.getIcons().add(icone);

		Scene scene = new Scene(root, 506, 486);
		primeiroEstagio.setTitle("Gestão de Inventário");
		primeiroEstagio.setScene(scene);
		primeiroEstagio.setResizable(false);

		// Configura o evento de fechamento da janela
		primeiroEstagio.setOnCloseRequest(event -> {
			ProvedorEntityManagerFactory.fecharEntityManagerFactory();
		});

		primeiroEstagio.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
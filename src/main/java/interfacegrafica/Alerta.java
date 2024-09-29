package interfacegrafica;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Alerta {
	// Criada para evitar repetição de código ao exibir um alerta
	public static void mostrarAlerta(AlertType tipo, String titulo, String cabecalho, String conteudo) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(cabecalho);
		alerta.setContentText(conteudo);
			
		if (tipo == AlertType.CONFIRMATION) {
			// Remove o botão "Cancelar"
			alerta.getButtonTypes().setAll(ButtonType.OK);
		}
		
		Image icone = new Image("/iconeCrud.png");
		Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(icone);
		
		alerta.showAndWait();
	}
}

	



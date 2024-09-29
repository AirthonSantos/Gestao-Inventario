package sistemagestao;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import interfacegrafica.Alerta;
import javafx.scene.control.Alert.AlertType;

public class Logging {
	private static final Logger logger = Logger.getLogger(Logging.class.getName());

	static {
		try {
			FileHandler fileHandler = new FileHandler("ArquivoLog.log", true);
			fileHandler.setEncoding("UTF-8");
			fileHandler.setFormatter(new SimpleFormatter());

			logger.addHandler(fileHandler);
			logger.setUseParentHandlers(false);

		} catch (IOException exceptionIo) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao criar o arquivo de log", null, "Erro ao configurar o FileHandler:" + exceptionIo.getMessage());
			ProvedorEntityManagerFactory.fecharEntityManagerFactory();
			System.exit(9);
		} catch (SecurityException exceptionSecurity) { 
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao criar o arquivo de log", null, "Erro de segurança ao configurar o FileHandler:" + exceptionSecurity.getMessage());
			ProvedorEntityManagerFactory.fecharEntityManagerFactory();
			System.exit(10);
		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao criar o arquivo de log", null, "Erro inesperado ao configurar o FileHandler:" + exception.getMessage());
			ProvedorEntityManagerFactory.fecharEntityManagerFactory();
			System.exit(1);
		}

	}

	public static Logger getLogger() {
		return logger;
	}

}

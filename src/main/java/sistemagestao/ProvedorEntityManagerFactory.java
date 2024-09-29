package sistemagestao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import interfacegrafica.Alerta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import javafx.scene.control.Alert.AlertType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// Classe Singleton
public class ProvedorEntityManagerFactory {
	private static EntityManagerFactory entityManagerFactory;
	private static Logger logger = Logging.getLogger();

	static {
		// Carrega o arquivo de configuração
		Properties credencias = new Properties();

		try {
			credencias.load(new FileInputStream("src/main/resources/credenciais.properties"));

		} catch (FileNotFoundException exceptionFileNotFound) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro relacionado ao arquivo de configuração", null, "Ocorreu um erro ao tentar ler o arquivo de configuração. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Arquivo de configuração não encontrado: %s. Verifique se o caminho está correto e se o arquivo existe.", exceptionFileNotFound.getMessage()));
			System.exit(2);

		} catch (IOException exceptionIO) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro relacionado ao arquivo de configuração", null, "Ocorreu um erro ao tentar ler o arquivo de configuração. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de entrada/saída ao tentar carregar o arquivo de configuração: %s. Verifique se o arquivo está acessível e se não há problemas de leitura.", exceptionIO));
			System.exit(3);

		} catch (SecurityException exceptionSecurity) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro relacionado ao arquivo de configuração", null, "Ocorreu um erro ao tentar ler o arquivo de configuração. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Permissão negada ao tentar acessar o arquivo de configuração: %s. Verifique as permissões do arquivo e do diretório.", exceptionSecurity));
			System.exit(4);

		} catch (IllegalArgumentException exceptionIllegal) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro relacionado ao arquivo de configuração", null, "Ocorreu um erro ao tentar ler o arquivo de configuração. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Caminho inválido para o arquivo de configuração: %s. Verifique se o caminho está correto e se não contém caracteres inválidos.", exceptionIllegal));
			System.exit(5);

		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro relacionado ao arquivo de configuração", null, "Ocorreu um erro ao tentar ler o arquivo de configuração. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao tentar carregar o arquivo de configuração: %s. Verifique o arquivo e as configurações do sistema.", exception));
			System.exit(1);

		}

		// Criar um objeto Properties para definir as configurações do JPA
		Properties propriedades = new Properties();

		String nome = credencias.getProperty("db.username");
		String senha = credencias.getProperty("db.password");
		String servidor = credencias.getProperty("db.server");
		String porta = credencias.getProperty("db.port");
		String bancoDados = credencias.getProperty("db.database");

		String url = String.format("jdbc:postgresql://%s:%s/%s", servidor, porta, bancoDados);

		propriedades.setProperty("jakarta.persistence.jdbc.url", url);
		propriedades.setProperty("jakarta.persistence.jdbc.user", nome);
		propriedades.setProperty("jakarta.persistence.jdbc.password", senha);

		// Tenta estabelecer a conexão com o PostgreSQL
		try {
			try (Connection connection = DriverManager.getConnection(url, nome, senha)) {
				entityManagerFactory = Persistence.createEntityManagerFactory("produtos", propriedades);
				EntityManager entityManager = entityManagerFactory.createEntityManager();

				// Tenta realizar uma operação simples
				entityManager.getTransaction().begin();
				entityManager.createNativeQuery("SELECT 1").getSingleResult();
				entityManager.getTransaction().commit();

				entityManager.close();
				logger.info("Objeto EntityManagerFactory criado com sucesso!");
			}        
		} catch (SQLException exceptionSQL) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao estabelecer a conexão com o banco de dados", null, "Não foi possivel estabelecer a conexão com o PostgreSQL. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro ao estabelecer a conexão com o banco de dados: %s. URL: %s. Verifique se o PostgreSQL está instalado e se as credenciais estão certas.", exceptionSQL.getMessage(), url));
			fecharEntityManagerFactory();
			System.exit(6);

		} catch (IllegalStateException exceptionIllegal) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao estabelecer a conexão com o banco de dados", null, "Não foi possivel estabelecer a conexão com o PostgreSQL. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de estado ilegal ao criar EntityManagerFactory ou EntityManager: %s. URL: %s. Verifique a configuração do banco de dados e se o PostgreSQL está em execução.", exceptionIllegal.getMessage(), url));
			fecharEntityManagerFactory();
			System.exit(7);

		} catch (PersistenceException exceptionPersistence) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao estabelecer a conexão com o banco de dados", null, "Não foi possivel estabelecer a conexão com o PostgreSQL. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de persistência ao criar EntityManagerFactory ou EntityManager: %s. URL: %s. Verifique se a configuração do banco de dados está correta e se o PostgreSQL está acessível.", exceptionPersistence.getMessage(), url));
			fecharEntityManagerFactory();
			System.exit(8);

		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao estabelecer a conexão com o banco de dados", null, "Não foi possivel estabelecer a conexão com o PostgreSQL. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao criar EntityManagerFactory ou EntityManager: %s. URL: %s. Verifique a configuração do banco de dados e o estado do PostgreSQL.", exception.getMessage(), url));
			fecharEntityManagerFactory();
			System.exit(1);
		}
	}
	
	public static EntityManagerFactory obterEntityManagerFactory() {
		return entityManagerFactory;
	}

	public static void fecharEntityManagerFactory() {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
			logger.info("Objeto EntityManagerFactory encerrado com sucesso!");
		}
	}
	
}

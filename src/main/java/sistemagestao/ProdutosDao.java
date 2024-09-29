package sistemagestao;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import javafx.scene.control.Alert.AlertType;

import interfacegrafica.Alerta;

// Classe DAO
public class ProdutosDao {
	private EntityManagerFactory entityManagerFactory = ProvedorEntityManagerFactory.obterEntityManagerFactory();
	private EntityManager entityManager;
	private EntityTransaction entityTransaction;
	private Logger logger = Logging.getLogger();
	
	public void reordenarProdutos() {
		// Reordena os produtos no banco de dados
		logger.info("Iniciando a reordenação dos produtos");

		List<Produtos> produtos = listar();
		Integer novaOrdem = 1;

		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();

			for (Produtos produto : produtos) {
				produto.setOrdem(novaOrdem++);
				entityManager.merge(produto);
			}

			entityTransaction.commit();
			logger.info("Reodenação concluída com sucesso!");

		} catch (IllegalArgumentException exceptionIllegal) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao reordenar os produtos", null, "Ocorreu um erro ao reordenar os produtos. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de argumento inválido ao tentar realizar o reordenamento dos produtos: %s", exceptionIllegal.getMessage()));

		} catch (EntityNotFoundException exceptionEntity) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao reordenar os produtos", null, "Ocorreu um erro ao reordenar os produtos. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de entidade não encontrada ao tentar realizar o reordenamento dos produtos: %s", exceptionEntity.getMessage()));

		} catch (OptimisticLockException exceptionOptimistic) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao reordenar os produtos", null, "Ocorreu um erro ao reordenar os produtos. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de conflito de atualização ao tentar realizar o reordenamento dos produtos: %s", exceptionOptimistic.getMessage()));

		} catch (TransactionRequiredException exceptionTransaction) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao reordenar os produtos", null, "Ocorreu um erro ao reordenar os produtos. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de transação ao tentar realizar o reordenamento dos produtos: %s", exceptionTransaction.getMessage()));

		} catch (PersistenceException exceptionPersistence) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao reordenar os produtos", null, "Ocorreu um erro ao reordenar os produtos. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de persistência ao tentar realizar o reordenamento dos produtos: %s", exceptionPersistence.getMessage()));

		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao reordenar os produtos", null, "Ocorreu um erro ao reordenar os produtos. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao tentar realizar o reordenamento dos produtos: %s", exception.getMessage()));

		} finally {
			if (entityTransaction != null && entityTransaction.isActive()) {
				entityTransaction.rollback();
			}

			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
	
	public Produtos consultaPorId(Long id) {
		// Recebe um id e retorna o produto correspondente no banco de dados
		logger.info("Iniciando o processo de consulta do produto por id");
		Produtos produto = null;

		try {		
			entityManager = entityManagerFactory.createEntityManager();
			produto = entityManager.find(Produtos.class, id);	

			logger.info("Consulta concluída com sucesso!");	

		} catch (IllegalArgumentException exceptionIllegal) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro na consulta", null, "Ocorreu um erro ao consultar um produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de argumento inválido ao tentar realizar uma consulta: %s", exceptionIllegal.getMessage()));

		} catch (EntityNotFoundException exceptionEntity) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro na consulta", null, "Ocorreu um erro ao consultar um produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de entidade não encontrada ao tentar realizar uma consulta: %s", exceptionEntity.getMessage()));

		} catch (TransactionRequiredException exceptionTransaction) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro na consulta", null, "Ocorreu um erro ao consultar um produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de Transação ao tentar realizar uma consulta: %s", exceptionTransaction.getMessage()));

		} catch (PersistenceException exceptionPersistence) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro na consulta", null, "Ocorreu um erro ao consultar um produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de persistência ao tentar realizar uma consulta: %s", exceptionPersistence.getMessage()));
			
		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro na consulta", null, "Ocorreu um erro ao consultar um produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao tentar realizar uma consulta: %s", exception.getMessage()));

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}

		return produto;
	}
	
	public boolean salvar(Produtos produto) {
		// Recebe um produto e o armazena no banco de dados
		logger.info(String.format("Iniciando o processo de salvamento do produto: %s", produto.getNome()));

		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();

			// Atribui uma sequência a coluna Ordem
			Integer maxOrdem = (Integer) entityManager.createQuery("SELECT MAX(ordem) FROM Produtos").getSingleResult();

			if (maxOrdem == null) {
				produto.setOrdem(1);
			} else {
				produto.setOrdem(maxOrdem + 1);
			}

			entityManager.persist(produto);
			entityTransaction.commit();

			logger.info(String.format("Salvamento do produto %s com id %d foi concluída com sucesso!", produto.getNome(), produto.getId()));
			return true;

		} catch (EntityExistsException exceptionEntity) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao salvar determinado produto", null, "Ocorreu um erro ao tentar salvar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de entidade já existente ao tentar salvar um produto: %s", exceptionEntity.getMessage()));
			return false;

		} catch (TransactionRequiredException exceptionTransaction) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao salvar determinado produto", null, "Ocorreu um erro ao tentar salvar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de transação ao tentar salvar um produto: %s", exceptionTransaction.getMessage()));
			return false;

		} catch (PersistenceException exceptionPersistence) { 
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao salvar determinado produto", null, "Ocorreu um erro ao tentar salvar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de persistência ao tentar salvar um produto: %s", exceptionPersistence.getMessage()));
			return false;

		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao salvar determinado produto", null, "Ocorreu um erro ao tentar salvar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao tentar salvar um produto: %s", exception.getMessage()));
			return false;

		} finally {
			if (entityTransaction != null && entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
			if (entityManager != null) {
				entityManager.close();
			}
		}

	}
		
	public List<Produtos> listar() {
		// Retorna uma lista com todos os produtos do banco de dados
		logger.info("Iniciando a listagem dos produtos");
		List<Produtos> resultado = new ArrayList<>();
		
		try {
			entityManager = entityManagerFactory.createEntityManager();
			resultado = entityManager.createQuery("FROM Produtos", Produtos.class).getResultList();
			logger.info("Listagem realizada com sucesso!");
			
		} catch (PersistenceException exceptionPersistence) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao listar os dados", null, "Ocorreu um erro ao tentar realizar a listagem. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de persistência ao tentar listar os produtos: %s", exceptionPersistence.getMessage()));
			
		} catch (IllegalArgumentException exceptionIllegal) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao listar os dados", null, "Ocorreu um erro ao tentar realizar a listagem. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de argumento inválido ao tentar listar os produtos: %s", exceptionIllegal.getMessage()));
			
		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao listar os dados", null, "Ocorreu um erro ao tentar realizar a listagem. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao tentar listar os produtos: %s", exception.getMessage()));
			
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		
		return resultado;
	}
	
	public boolean atualizar(Produtos produto) {
		// Atualiza o produto correspondente no banco de dados
		logger.info(String.format("Inicio da atualização do produto de id: %d", produto.getId()));

		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityTransaction = entityManager.getTransaction();	

			entityTransaction.begin();
			entityManager.merge(produto);	
			entityTransaction.commit();

			logger.info(String.format("Atualização do produto %d concluída com sucesso", produto.getId()));
			return true;

		} catch (OptimisticLockException exceptionOptimistic) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao atualizar determinado produto", null, "Ocorreu um erro ao tentar atualizar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de conflito de atualização ao tentar atualizar determinado produto: %s", exceptionOptimistic.getMessage()));
			return false;

		} catch (IllegalArgumentException exceptionIllegal) { 
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao atualizar determinado produto", null, "Ocorreu um erro ao tentar atualizar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de argumento inválido ao tentar atualizar determinado produto: %s", exceptionIllegal.getMessage()));
			return false;

		} catch (TransactionRequiredException exceptionTransaction) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao atualizar determinado produto", null, "Ocorreu um erro ao tentar atualizar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de transação ao tentar atualizar determinado produto: %s", exceptionTransaction.getMessage()));
			return false;

		} catch (PersistenceException exceptionPersistence) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao atualizar determinado produto", null, "Ocorreu um erro ao tentar atualizar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de persistência ao tentar atualizar determinado produto: %s", exceptionPersistence.getMessage()));
			return false;

		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao atualizar determinado produto", null, "Ocorreu um erro ao tentar atualizar determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao tentar atualizar determinado produto: %s", exception.getMessage()));
			return false;

		} finally {
			if (entityTransaction != null && entityTransaction.isActive()) {
				entityTransaction.rollback();
			}

			if (entityManager != null) {
				entityManager.close();
			}
		}
		
	}

	public boolean remover(Long id) {
		// Recebe um id e remove o produto correspondente no banco de dados
		logger.info(String.format("Inicio do processo de remoção do id: %d", id));

		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityTransaction = entityManager.getTransaction();

			Produtos produto = entityManager.find(Produtos.class, id);

			entityTransaction.begin();
			entityManager.remove(produto);
			entityTransaction.commit();

			reordenarProdutos();

			logger.info(String.format("Remoção do id %d realizada com sucesso", id));
			return true;

		} catch (OptimisticLockException exceptionOptimistic) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao realiza a remoção", null, "Ocorreu um erro ao tentar remover determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de conflito de atualização ao tentar remover determinado produto: %s", exceptionOptimistic.getMessage()));
			return false;

		} catch (IllegalArgumentException exceptionIllegal) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao realiza a remoção", null, "Ocorreu um erro ao tentar remover determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de argumento ilegal ao tentar remover determinado produto: %s", exceptionIllegal.getMessage()));
			return false;

		} catch (TransactionRequiredException exceptionTransaction) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao realiza a remoção", null, "Ocorreu um erro ao tentar remover determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de transação necessária ao tentar remover determinado produto: %s", exceptionTransaction.getMessage()));
			return false;

		} catch (PersistenceException exceptionPersistence) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao realiza a remoção", null, "Ocorreu um erro ao tentar remover determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro de persistência ao tentar remover determinado produto: %s", exceptionPersistence.getMessage()));
			return false;

		} catch (Exception exception) {
			Alerta.mostrarAlerta(AlertType.ERROR, "Erro ao realiza a remoção", null, "Ocorreu um erro ao tentar remover determinado produto. Para mais informações acesse o arquivo de log.");
			logger.severe(String.format("Erro inesperado ao tentar remover determinado produto: %s", exception.getMessage()));
			return false;

		} finally {
			if (entityTransaction != null && entityTransaction.isActive()) {
				entityTransaction.rollback();
			}

			if (entityManager != null) {
				entityManager.close();
			}
		}

	}

}

package sistemagestao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "produtos")
public class Produtos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Ordem")
	private Integer ordem;

	@Column(name = "Nome")
	private String nome;

	@Column(name = "Quantidade")
	private Integer quantidade;

	@Column(name = "Preco")
	private Double preco;

	// Construtores
	public Produtos() {

	}

	public Produtos(String nome, Integer quantidade, Double preco) {
		this.nome = nome;
		this.quantidade = quantidade;
		this.preco = preco;
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}
}

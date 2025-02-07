package br.com.drs.cadastrotelefonico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cadastro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O CPF não pode ser vazio.")
    @Pattern(regexp = "^(\\d{3})\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "O CPF deve estar no formato xxx.xxx.xxx-xx.")
    private String cpf;


    @Column(unique = true)
    @Pattern(regexp = "^$|^\\(?\\d{2}\\)?\\s?\\d{4,5}-\\d{4}$", message = "O telefone deve estar no formato (xx) xxxx-xxxx.")
    private String telefone;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O celular não pode ser vazio.")
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-\\d{4}$", message = "O celular deve estar no formato (xx) xxxxx-xxxx.")
    private String celular;

    private boolean ativo;

    private boolean favorito;
}


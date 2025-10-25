# Documento de Projeto: Sistema de Adoção de Animais

Este documento detalha o plano de implementação, a arquitetura e exemplos de código para o desenvolvimento do sistema, com base nos requisitos fornecidos.

## 1. Plano de Desenvolvimento (Passo a Passo)

Esta é uma sugestão de plano para dividir o projeto em fases gerenciáveis.

### Fase 1: Fundação e Modelagem de Domínio
1.  **Configuração do Projeto:** Definir a estrutura de pacotes (ex: `domain`, `repository`, `service`, `ui`).
2.  **Modelagem (Entidades e Contratos):**
    * [cite_start]Criar classe abstrata `Animal` (com `emitirSom` abstrato). [cite: 39]
    * [cite_start]Criar subclasses `Cachorro` e `Gato` (sobrescrevendo `emitirSom`). [cite: 40]
    * Criar interface `CuidadosEspeciais` (com `vacinar`, `vermifugar`).
    * Criar classe `Adotante` (garantindo encapsulamento).
    * [cite_start]Criar classe `Adocao` (ligando `Animal` + `Adotante`, sobrescrevendo `toString`). [cite: 41]
3.  **Modelagem (Exceções):**
    * [cite_start]Criar `LimiteAdocoesException`. [cite: 42]
    * [cite_start]Criar `AnimalIndisponivelException`. [cite: 42]

### Fase 2: Camada de Persistência (Repositório)
1.  **Definir Contratos:** Criar interfaces para os repositórios (ex: `IAnimalRepositorio`, `IAdotanteRepositorio`).
2.  [cite_start]**Implementar Repositórios Concretos:** Criar classes que implementam as interfaces usando a estratégia escolhida (CSV, JSON, etc.). [cite: 23, 24, 25, 26]
3.  [cite_start]**Gerenciamento de Carga:** Garantir que os dados sejam carregados na inicialização do sistema. [cite: 29]
4.  [cite_start]**Gerenciamento de Salvamento:** Garantir que os dados sejam salvos após as operações. [cite: 30]

### Fase 3: Camada de Serviço (Lógica de Negócio)
1.  [cite_start]**Implementar Serviços CRUD:** Criar classes de serviço (ex: `AnimalService`) que usam os repositórios. [cite: 14, 15]
2.  [cite_start]**Implementar Fluxo de Adoção:** Criar um `AdocaoService` para orquestrar o processo de adoção. [cite: 16]
3.  [cite_start]**Validação:** Implementar as regras de negócio [cite: 19, 20] [cite_start]e lançar as exceções personalizadas quando violadas. [cite: 21, 48, 49]
4.  [cite_start]**Listagem e Filtros:** Implementar a listagem de adoções com filtros. [cite: 17]

### Fase 4: Interface de Usuário (CLI Obrigatória)
1.  [cite_start]**Menu Principal:** Criar um menu em loop no console. [cite: 33]
2.  **Submenus CRUD:** Implementar as telas de console para as operações de CRUD.
3.  **Telas de Adoção:** Implementar as telas para realizar e listar adoções.
4.  [cite_start]**Tratamento de Exceções:** Usar blocos `try-catch` na UI para capturar as exceções personalizadas e exibir mensagens amigáveis. [cite: 21]

### Fase 5: Bônus (GUI) e Finalização (Opcional)
1.  [cite_start]**(Opcional) Interface Gráfica (GUI):** Desenvolver uma GUI (JavaFX ou Swing). [cite: 34]
2.  [cite_start]**Reutilização:** Garantir que a GUI utilize as mesmas classes de Serviço e Repositório. [cite: 37]
3.  [cite_start]**Entrega:** Organizar o código-fonte em pacotes [cite: 59] [cite_start]e finalizar o `README.md`. [cite: 60]

---

## 2. Estrutura de POO Exigida (Conceitos)

* [cite_start]**Classe Abstrata `Animal`:** Classe mãe com atributos comuns e o método abstrato `emitirSom()`. [cite: 39]
* [cite_start]**Subclasses `Cachorro` e `Gato`:** Herdam de `Animal` e sobrescrevem `emitirSom()`. [cite: 40]
* **Interface `CuidadosEspeciais`:** Define métodos como `vacinar()` e `vermifugar()`.
* **Classe `Adotante`:** Deve ter atributos privados (encapsulamento) e pode ter construtores sobrecarregados.
* [cite_start]**Classe `Adocao`:** Liga `Animal` e `Adotante` e deve sobrescrever `toString()`. [cite: 41]
* [cite_start]**Exceptions Personalizadas:** `LimiteAdocoesException` e `AnimalIndisponivelException`. [cite: 42]
* [cite_start]**Sobrecarga:** Exigida em construtores e métodos de busca. [cite: 42]
* [cite_start]**Sobrescrita:** Exigida em `emitirSom()` e `toString()`. [cite: 43]

---

## 3. Exemplo de Código: Camada de Persistência (Repositório)

A camada de persistência é composta por uma interface (o contrato) e uma implementação (o código que de fato salva).

### 3.1. Interface `IAnimalRepositorio` (O Contrato)

```java
// Pacote: br.org.projeto.repository
import java.util.List;
import br.org.projeto.domain.Animal;

/**
 * Interface que define as operações de persistência para Animais.
 */
public interface IAnimalRepositorio {
    
    void salvar(Animal animal);
    void atualizar(Animal animal);
    void remover(int id);
    Animal buscarPorId(int id);
    List<Animal> listarTodos();
    
    // Método para cumprir requisito de sobrecarga de busca
    List<Animal> buscarPorNome(String nome);
}
```

### 3.2. Implementação `AnimalRepositorioJSON` (Exemplo)

Esta classe implementa a interface `IAnimalRepositorio` usando arquivos JSON. Ela requer a biblioteca **Gson**.

**Ajustes nas Entidades (Necessário para JSON):**

```java
// Na classe abstrata Animal
public abstract class Animal {
    // ... id, nome, idade, raca, status ...
    private String tipo; // Campo para identificar a subclasse no JSON

    public Animal(int id, String nome, int idade, String raca, String status, String tipo) {
        // ...
        this.tipo = tipo;
    }
    public String getTipo() { return tipo; }
    // ...
}

// Na subclasse Cachorro
public class Cachorro extends Animal {
    public Cachorro(int id, String nome, int idade, String raca, String status) {
        super(id, nome, idade, raca, status, "CACHORRO"); 
    }
    // ...
}

// Na subclasse Gato
public class Gato extends Animal {
    public Gato(int id, String nome, int idade, String raca, String status) {
        super(id, nome, idade, raca, status, "GATO");
    }
    // ...
}
```

**Classe `AnimalRepositorioJSON`:**

```java
// Pacote: br.org.projeto.repository
import br.org.projeto.domain.Animal;
import br.org.projeto.domain.Cachorro;
import br.org.projeto.domain.Gato;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnimalRepositorioJSON implements IAnimalRepositorio {

    private final String filePath;
    private final List<Animal> animaisCache;
    private final Gson gson;

    public AnimalRepositorioJSON(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.animaisCache = new ArrayList<>();
        carregarDados(); [cite_start]// [cite: 29]
    }

    private void carregarDados() {
        try (Reader reader = new FileReader(filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String tipo = jsonObject.get("tipo").getAsString();
                
                Animal animal = null;
                if ("CACHORRO".equals(tipo)) {
                    animal = gson.fromJson(jsonObject, Cachorro.class);
                } else if ("GATO".equals(tipo)) {
                    animal = gson.fromJson(jsonObject, Gato.class);
                }
                if (animal != null) {
                    animaisCache.add(animal);
                }
            }
        } catch (IOException e) {
            // Arquivo não existe ou vazio, inicia com lista vazia
        }
    }

    [cite_start]private void salvarDados() { // [cite: 30]
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(animaisCache, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no JSON: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Animal animal) {
        animaisCache.add(animal);
        salvarDados();
    }

    @Override
    public void atualizar(Animal animalAtualizado) {
        Optional<Animal> animalExistente = buscarPorId(animalAtualizado.getId());
        if (animalExistente.isPresent()) {
            animaisCache.remove(animalExistente.get());
            animaisCache.add(animalAtualizado);
            salvarDados();
        }
    }

    @Override
    public void remover(int id) {
        boolean removido = animaisCache.removeIf(animal -> animal.getId() == id);
        if (removido) {
            salvarDados();
        }
    }

    @Override
    public Animal buscarPorId(int id) {
        return animaisCache.stream()
                .filter(animal -> animal.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Animal> listarTodos() {
        return new ArrayList<>(animaisCache);
    }

    @Override
    public List<Animal> buscarPorNome(String nome) {
        return animaisCache.stream()
                .filter(animal -> animal.getNome().equalsIgnoreCase(nome))
                .collect(Collectors.toList());
    }
}
```

---

## 4. Exemplo de Código: Camada de Serviço (`AnimalService`)

Esta classe contém a lógica de negócio do CRUD e consome a interface `IAnimalRepositorio`.

```java
// Pacote: br.org.projeto.service
import java.util.List;
import br.org.projeto.domain.Animal;
import br.org.projeto.repository.IAnimalRepositorio;

/**
 * Classe de Serviço que gerencia a lógica de negócio para o CRUD de Animais.
 */
public class AnimalService {

    private IAnimalRepositorio animalRepositorio;

    /**
     * Construtor que recebe a implementação do repositório (Injeção de Dependência).
     */
    public AnimalService(IAnimalRepositorio animalRepositorio) {
        this.animalRepositorio = animalRepositorio;
    }

    // --- C: CREATE (Cadastrar) ---
    public void cadastrarAnimal(Animal animal) {
        // Validações de negócio (ex: nome vazio) podem ir aqui.
        animalRepositorio.salvar(animal);
        System.out.println("Animal cadastrado com sucesso!");
    }

    // --- R: READ (Listar/Buscar) ---
    public List<Animal> listarAnimais() {
        return animalRepositorio.listarTodos();
    }

    /**
     * Busca um animal específico pelo seu ID.
     */
    public Animal buscarAnimalPorId(int id) {
        return animalRepositorio.buscarPorId(id);
    }
    
    /**
     * Busca animais pelo nome.
     * [cite_start]Exemplo de SOBRECARGA (Overload) de método de busca. [cite: 42]
     */
    public List<Animal> buscarAnimalPorNome(String nome) {
        return animalRepositorio.buscarPorNome(nome);
    }

    // --- U: UPDATE (Atualizar) ---
    public void atualizarAnimal(Animal animal) {
        Animal animalExistente = animalRepositorio.buscarPorId(animal.getId());
        if (animalExistente == null) {
            System.out.println("Erro: Animal com ID " + animal.getId() + " não encontrado.");
            return;
        }
        animalRepositorio.atualizar(animal);
        System.out.println("Animal atualizado com sucesso!");
    }

    // --- D: DELETE (Remover) ---
    public void removerAnimal(int id) {
        Animal animalParaRemover = animalRepositorio.buscarPorId(id);
        if (animalParaRemover == null) {
            System.out.println("Erro: Animal com ID " + id + " não encontrado.");
            return;
        }

        // Regra de Negócio: Não permitir excluir animal já adotado.
        [cite_start]if (animalParaRemover.getStatus().equals("ADOTADO")) { // [cite: 20]
             System.out.println("Erro: Não é possível remover um animal que já foi adotado.");
             return;
        }
        
        animalRepositorio.remover(id);
        System.out.println("Animal removido com sucesso!");
    }
}
```

---

## 5. Exemplo de Código: Interface (UI)

### 5.1. [cite_start]CLI (Obrigatório) [cite: 33]

Este é um exemplo de como o `AnimalService` seria usado na classe `MainCLI`.

```java
// Pacote: br.org.projeto.ui
import br.org.projeto.domain.Cachorro; [cite_start]// [cite: 40]
import br.org.projeto.domain.Animal; [cite_start]// [cite: 39]
import br.org.projeto.domain.Gato; [cite_start]// [cite: 40]
import br.org.projeto.repository.IAnimalRepositorio;
import br.org.projeto.repository.AnimalRepositorioJSON; [cite_start]// Usando a implementação JSON [cite: 24]
import br.org.projeto.service.AnimalService;
import java.util.List;

public class MainCLI {

    public static void main(String[] args) {
        
        // 1. Inicializa a persistência (Ex: usando JSON)
        IAnimalRepositorio meuRepositorio = new AnimalRepositorioJSON("animais.json");

        // 2. Inicializa o Serviço, injetando o repositório
        AnimalService animalService = new AnimalService(meuRepositorio);

        // --- Exemplo de uso do CRUD ---
        // (O ideal é ter um menu interativo aqui)

        [cite_start]// C (Create) [cite: 14]
        System.out.println("Cadastrando animais...");
        Animal rex = new Cachorro(1, "Rex", 3, "Vira-lata", "DISPONIVEL"); [cite_start]// [cite: 20]
        Animal felix = new Gato(2, "Felix", 2, "Siamês", "DISPONIVEL"); [cite_start]// [cite: 20]
        
        // Limpa o repositório para este exemplo (opcional)
        // animalService.removerAnimal(1);
        // animalService.removerAnimal(2);
        
        animalService.cadastrarAnimal(rex);
        animalService.cadastrarAnimal(felix);
        
        System.out.println("--------------------");

        [cite_start]// R (Read - Listar) [cite: 14]
        System.out.println("Listando todos os animais:");
        List<Animal> animais = animalService.listarAnimais();
        for (Animal animal : animais) {
            System.out.println(animal.getNome() + " (" + animal.getClass().getSimpleName() + ")");
            animal.emitirSom(); [cite_start]// [cite: 40, 43]
        }

        System.out.println("--------------------");

        [cite_start]// U (Update) [cite: 14]
        System.out.println("Atualizando o Rex...");
        rex.setIdade(4); 
        animalService.atualizarAnimal(rex);

        // R (Read - Buscar por ID)
        Animal animalBuscado = animalService.buscarAnimalPorId(1);
        System.out.println("Idade atualizada do Rex: " + animalBuscado.getIdade());

        System.out.println("--------------------");

        [cite_start]// D (Delete) [cite: 14]
        System.out.println("Removendo o Felix...");
        animalService.removerAnimal(2);

        System.out.println("--------------------");
        System.out.println("Listagem final:");
        for (Animal animal : animalService.listarAnimais()) {
            System.out.println(animal.getNome());
        }
    }
}
```

### 5.2. [cite_start]JavaFX (Opcional/Bônus) [cite: 34]

[cite_start]Este é um exemplo de GUI para o CRUD de Animais, reutilizando `AnimalService` e `IAnimalRepositorio`. [cite: 37]

```java
// Pacote: br.org.projeto.ui
import br.org.projeto.domain.Animal;
import br.org.projeto.domain.Cachorro;
import br.org.projeto.domain.Gato;
import br.org.projeto.repository.AnimalRepositorioJSON;
import br.org.projeto.repository.IAnimalRepositorio;
import br.org.projeto.service.AnimalService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainJavaFX extends Application {

    // 1. Inicializa o Serviço e Repositório
    private IAnimalRepositorio meuRepositorio = new AnimalRepositorioJSON("animais.json");
    private AnimalService animalService = new AnimalService(meuRepositorio);

    private TableView<Animal> animalTable = new TableView<>();
    private ObservableList<Animal> animalData;

    // Campos do Formulário
    private TextField idField = new TextField();
    private TextField nomeField = new TextField();
    private TextField idadeField = new TextField();
    private TextField racaField = new TextField();
    private ComboBox<String> tipoBox = new ComboBox<>();
    private ComboBox<String> statusBox = new ComboBox<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Adoção de Animais");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        setupTable();
        root.setCenter(animalTable);

        VBox formBox = createCrudForm();
        root.setRight(formBox);

        refreshTable();
        setupTableSelectionListener();

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTable() {
        TableColumn<Animal, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Animal, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Animal, Integer> idadeCol = new TableColumn<>("Idade");
        idadeCol.setCellValueFactory(new PropertyValueFactory<>("idade"));
        TableColumn<Animal, String> racaCol = new TableColumn<>("Raça");
        racaCol.setCellValueFactory(new PropertyValueFactory<>("raca"));
        TableColumn<Animal, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Animal, String> tipoCol = new TableColumn<>("Tipo");
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("tipo")); // Usa o campo 'tipo'

        animalTable.getColumns().addAll(idCol, nomeCol, idadeCol, racaCol, statusCol, tipoCol);
    }

    private VBox createCrudForm() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Nome:"), 0, 1);
        grid.add(nomeField, 1, 1);
        grid.add(new Label("Idade:"), 0, 2);
        grid.add(idadeField, 1, 2);
        grid.add(new Label("Raça:"), 0, 3);
        grid.add(racaField, 1, 3);
        grid.add(new Label("Tipo:"), 0, 4);
        tipoBox.getItems().addAll("Cachorro", "Gato");
        grid.add(tipoBox, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        statusBox.getItems().addAll("DISPONIVEL", "ADOTADO");
        statusBox.setValue("DISPONIVEL");
        grid.add(statusBox, 1, 5);
        
        idField.setPromptText("ID (para cadastrar)");

        HBox buttonBox = new HBox(10);
        Button cadastrarButton = new Button("Cadastrar");
        Button atualizarButton = new Button("Atualizar");
        Button removerButton = new Button("Remover");
        Button limparButton = new Button("Limpar");
        buttonBox.getChildren().addAll(cadastrarButton, atualizarButton, removerButton, limparButton);

        cadastrarButton.setOnAction(e -> handleCadastrar());
        atualizarButton.setOnAction(e -> handleAtualizar());
        removerButton.setOnAction(e -> handleRemover());
        limparButton.setOnAction(e -> limparCampos());

        vbox.getChildren().addAll(new Label("Gerenciar Animal"), grid, buttonBox);
        return vbox;
    }

    private void refreshTable() {
        animalData = FXCollections.observableArrayList(animalService.listarAnimais());
        animalTable.setItems(animalData);
    }

    private void setupTableSelectionListener() {
        animalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                popularFormulario(newSelection);
            }
        });
    }

    private void handleCadastrar() {
        try {
            int id = Integer.parseInt(idField.getText());
            String nome = nomeField.getText();
            int idade = Integer.parseInt(idadeField.getText());
            String raca = racaField.getText();
            String status = statusBox.getValue();
            String tipo = tipoBox.getValue();

            Animal novoAnimal;
            if ("Cachorro".equals(tipo)) {
                novoAnimal = new Cachorro(id, nome, idade, raca, status);
            } else if ("Gato".equals(tipo)) {
                novoAnimal = new Gato(id, nome, idade, raca, status);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Tipo de animal inválido.");
                return;
            }

            animalService.cadastrarAnimal(novoAnimal);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Animal cadastrado!");
            refreshTable();
            limparCampos();

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar: " + ex.getMessage());
        }
    }

    private void handleAtualizar() {
        Animal animalSelecionado = animalTable.getSelectionModel().getSelectedItem();
        if (animalSelecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um animal para atualizar.");
            return;
        }
        
        try {
            animalSelecionado.setNome(nomeField.getText());
            animalSelecionado.setIdade(Integer.parseInt(idadeField.getText()));
            // Assumindo que Animal tem setRaca e setStatus
            // animalSelecionado.setRaca(racaField.getText()); 
            // animalSelecionado.setStatus(statusBox.getValue());
            
            animalService.atualizarAnimal(animalSelecionado);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Animal atualizado!");
            refreshTable();
            limparCampos();

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar: " + ex.getMessage());
        }
    }
    
    private void handleRemover() {
        Animal animalSelecionado = animalTable.getSelectionModel().getSelectedItem();
        if (animalSelecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um animal para remover.");
            return;
        }

        try {
            animalService.removerAnimal(animalSelecionado.getId());
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Animal removido!");
            refreshTable();
            limparCampos();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover: " + ex.getMessage());
        }
    }

    private void popularFormulario(Animal animal) {
        idField.setText(String.valueOf(animal.getId()));
        nomeField.setText(animal.getNome());
        idadeField.setText(String.valueOf(animal.getIdade()));
        // racaField.setText(animal.getRaca());
        statusBox.setValue(animal.getStatus());
        tipoBox.setValue(animal.getTipo());
        idField.setEditable(false);
    }

    private void limparCampos() {
        idField.clear();
        nomeField.clear();
        idadeField.clear();
        racaField.clear();
        tipoBox.setValue(null);
        statusBox.setValue("DISPONIVEL");
        idField.setEditable(true);
        animalTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
```

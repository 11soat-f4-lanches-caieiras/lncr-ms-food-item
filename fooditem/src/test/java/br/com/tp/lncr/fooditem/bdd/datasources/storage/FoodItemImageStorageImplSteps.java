package br.com.tp.lncr.fooditem.bdd.datasources.storage;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import br.com.tp.lncr.core.exceptions.FoodItemException;
import br.com.tp.lncr.fooditem.configs.FoodItemConfig;
import br.com.tp.lncr.fooditem.datasources.storage.FoodItemImageStorageImpl;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FoodItemImageStorageImplSteps {

    private FoodItemImageStorageImpl storage;
    private FoodItemConfig foodItemConfig;
    private String testDirectory;
    private String base64Data;
    private String fileName;
    private List<FoodItemImageDTO> imageList;
    private String retrievedData;
    private Exception thrownException;

    @Before
    public void setUp() throws IOException {
        testDirectory = "target/test-images/";
        foodItemConfig = new FoodItemConfig();
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setDirectory(testDirectory);
        foodItemConfig.setImage(imageConfig);

        storage = new FoodItemImageStorageImpl(foodItemConfig);

        // Limpar diretório de testes
        Path testPath = Path.of(testDirectory);
        if (Files.exists(testPath)) {
            Files.walk(testPath)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        // Ignorar erros de limpeza
                    }
                });
        }

        imageList = new ArrayList<>();
        thrownException = null;
        base64Data = Base64.getEncoder().encodeToString("test image data".getBytes());
    }

    @After
    public void tearDown() throws IOException {
        // Limpar após testes
        Path testPath = Path.of(testDirectory);
        if (Files.exists(testPath)) {
            Files.walk(testPath)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        // Ignorar erros de limpeza
                    }
                });
        }
    }

    @Dado("que tenho dados de imagem em base64")
    public void quetenhoTadosDeImagemEmBase64() {
        base64Data = Base64.getEncoder().encodeToString("test image data content".getBytes());
        assertNotNull(base64Data);
    }

    @Dado("um nome de arquivo {string}")
    public void umNomeDeArquivo(String nomeArquivo) {
        fileName = nomeArquivo;
    }

    @Quando("eu salvar a imagem")
    public void euSalvarAImagem() {
        try {
            storage.saveImageFile(base64Data, fileName);
        } catch (IOException e) {
            thrownException = e;
        }
    }

    @Então("a imagem deve ser salva no diretório configurado")
    public void aImagemDeveSerSalvaNodiretorioConfigurado() {
        File file = new File(testDirectory + fileName);
        assertTrue(file.exists(), "O arquivo deveria existir");
    }

    @Então("o arquivo deve existir no sistema de arquivos")
    public void oArquivoDeveExistirNoSistemaDeArquivos() {
        File file = new File(testDirectory + fileName);
        assertTrue(file.exists() && file.isFile());
    }

    @Dado("que tenho uma lista com {int} imagens")
    public void queTenhoUmaListaComImagens(Integer quantidade) {
        imageList = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            FoodItemImageDTO image = new FoodItemImageDTO();
            image.setFileName("image_" + i + ".jpg");
            image.setData(Base64.getEncoder().encodeToString(("test data " + i).getBytes()));
            imageList.add(image);
        }
    }

    @Quando("eu salvar todas as imagens")
    public void euSalvarTodasAsImagens() {
        storage.saveImagesFiles(imageList);
    }

    @Então("todas as {int} imagens devem ser salvas no diretório")
    public void todasAsImagensDevemSerSalvasNoDiretorio(Integer quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            File file = new File(testDirectory + "image_" + i + ".jpg");
            assertTrue(file.exists(), "Arquivo image_" + i + ".jpg deveria existir");
        }
    }

    @Dado("que existe uma imagem {string} salva no diretório")
    public void queExisteUmaImagemSalvaNoDiretorio(String nomeArquivo) throws IOException {
        File directory = new File(testDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(testDirectory + nomeArquivo);
        Files.write(file.toPath(), "test image content".getBytes());
        assertTrue(file.exists());
    }

    @Quando("eu recuperar os dados da imagem {string}")
    public void euRecuperarOsDadosDaImagem(String nomeArquivo) {
        try {
            retrievedData = storage.getImgaeData(nomeArquivo);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("devo receber os dados em formato base64")
    public void devoReceberOsDadosEmFormatoBase64() {
        assertNotNull(retrievedData);
        assertDoesNotThrow(() -> Base64.getDecoder().decode(retrievedData));
    }

    @Então("os dados não devem estar vazios")
    public void osDadosNaoDevemEstarVazios() {
        assertNotNull(retrievedData);
        assertFalse(retrievedData.isEmpty());
    }

    @Dado("que existem {int} imagens salvas no diretório")
    public void queExistemImagensSalvasNoDiretorio(Integer quantidade) throws IOException {
        File directory = new File(testDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        imageList = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            FoodItemImageDTO image = new FoodItemImageDTO();
            String filename = "saved_image_" + i + ".jpg";
            image.setFileName(filename);
            imageList.add(image);

            File file = new File(testDirectory + filename);
            Files.write(file.toPath(), ("content " + i).getBytes());
        }
    }

    @Quando("eu recuperar os dados de todas as imagens")
    public void euRecuperarOsDadosDeTodasAsImagens() {
        storage.getImagesFiles(imageList);
    }

    @Então("todas as imagens devem ter dados preenchidos")
    public void todasAsImagensDevemTerDadosPreenchidos() {
        for (FoodItemImageDTO image : imageList) {
            assertNotNull(image.getData(), "Dados da imagem " + image.getFileName() + " devem estar preenchidos");
            assertFalse(image.getData().isEmpty());
        }
    }

    @Dado("que a imagem {string} não existe no diretório")
    public void queAImagemNaoExisteNoDiretorio(String nomeArquivo) {
        File file = new File(testDirectory + nomeArquivo);
        assertFalse(file.exists());
    }

    @Quando("eu tentar recuperar os dados da imagem {string}")
    public void euTentarRecuperarOsDadosDaImagem(String nomeArquivo) {
        try {
            retrievedData = storage.getImgaeData(nomeArquivo);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("uma exceção FoodItemException deve ser lançada")
    public void umaExcecaoFoodItemExceptionDeveSerLancada() {
        assertNotNull(thrownException);
        assertTrue(thrownException instanceof FoodItemException);
    }

    @Então("o código de erro deve ser {int}")
    public void oCodigoDeErroDeveSer(Integer codigo) {
        assertTrue(thrownException instanceof FoodItemException);
        assertEquals(codigo, ((FoodItemException) thrownException).getCode());
    }

    @Quando("eu deletar a imagem {string}")
    public void euDeletarAImagem(String nomeArquivo) {
        storage.deleteImageFile(nomeArquivo);
    }

    @Então("a imagem {string} não deve mais existir no sistema de arquivos")
    public void aImagemNaoDeveMaisExistirNoSistemaDeArquivos(String nomeArquivo) {
        File file = new File(testDirectory + nomeArquivo);
        File directory = new File(testDirectory);
        assertFalse(file.exists(), "A imagem " + nomeArquivo + " deveria ter sido deletada");
        assertTrue(directory.exists(), "O diretório pai deveria ainda existir");
    }

    @Dado("que existem {int} imagens para deletar")
    public void queExistemImagensParaDeletar(Integer quantidade) throws IOException {
        File directory = new File(testDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        imageList = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            FoodItemImageDTO image = new FoodItemImageDTO();
            String filename = "delete_image_" + i + ".jpg";
            image.setFileName(filename);
            imageList.add(image);

            File file = new File(testDirectory + filename);
            Files.write(file.toPath(), "content to delete".getBytes());
            assertTrue(file.exists());
        }
    }

    @Quando("eu deletar todas as imagens")
    public void euDeletarTodasAsImagens() {
        storage.deleteImagesFiles(imageList);
    }

    @Então("nenhuma das imagens deve existir mais no sistema de arquivos")
    public void nenhumaDasImagensDeveExistirMaisNoSistemaDeArquivos() {
        for (FoodItemImageDTO image : imageList) {
            File file = new File(testDirectory + image.getFileName());
            assertFalse(file.exists(), "Arquivo " + image.getFileName() + " não deveria existir");
        }
    }

    @Dado("que a imagem {string} não existe")
    public void queAImagemNaoExiste(String nomeArquivo) {
        File file = new File(testDirectory + nomeArquivo);
        if (file.exists()) {
            file.delete();
        }
        assertFalse(file.exists());
    }

    @Quando("eu tentar deletar a imagem {string}")
    public void euTentarDeletarAImagem(String nomeArquivo) {
        try {
            storage.deleteImageFile(nomeArquivo);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("nenhuma exceção deve ser lançada")
    public void nenhumaExcecaoDeveSerLancada() {
        assertNull(thrownException);
    }

    @Dado("que o diretório de imagens não existe")
    public void queODiretorioDeImagensNaoExiste() throws IOException {
        Path testPath = Path.of(testDirectory);
        if (Files.exists(testPath)) {
            Files.walk(testPath)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        // Ignorar
                    }
                });
        }
        assertFalse(Files.exists(testPath));
    }

    @Dado("tenho dados de imagem válidos")
    public void tenhoDadosDeImagemValidos() {
        base64Data = Base64.getEncoder().encodeToString("valid image data".getBytes());
        fileName = "auto-created-dir-test.jpg";
    }

    @Então("o diretório deve ser criado automaticamente")
    public void oDiretorioDeveSerCriadoAutomaticamente() {
        File directory = new File(testDirectory);
        assertTrue(directory.exists() && directory.isDirectory());
    }

    @Então("a imagem deve ser salva com sucesso")
    public void aImagemDeveSerSalvaComSucesso() {
        File file = new File(testDirectory + fileName);
        assertTrue(file.exists() && file.isFile(), "O arquivo deveria existir e ser um arquivo válido");
        assertTrue(file.length() > 0, "O arquivo deveria ter conteúdo");
    }
}


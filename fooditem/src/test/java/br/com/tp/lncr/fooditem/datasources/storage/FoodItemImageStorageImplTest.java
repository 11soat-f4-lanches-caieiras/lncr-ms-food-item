package br.com.tp.lncr.fooditem.datasources.storage;

import br.com.tp.lncr.core.dtos.fooditem.FoodItemImageDTO;
import br.com.tp.lncr.core.exceptions.FoodItemException;
import br.com.tp.lncr.fooditem.configs.FoodItemConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FoodItemImageStorageImpl Tests")
class FoodItemImageStorageImplTest {

    private FoodItemImageStorageImpl storage;
    private FoodItemConfig foodItemConfig;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        foodItemConfig = new FoodItemConfig();
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setDirectory(tempDir.toString() + "/");
        foodItemConfig.setImage(imageConfig);
        storage = new FoodItemImageStorageImpl(foodItemConfig);
    }

    @Test
    @DisplayName("Deve salvar uma imagem com sucesso")
    void testSaveImageFile_Success() throws IOException {
        // Arrange
        String fileName = "test-image.jpg";
        String base64Data = Base64.getEncoder().encodeToString("test image content".getBytes());

        // Act
        storage.saveImageFile(base64Data, fileName);

        // Assert
        Path savedFile = tempDir.resolve(fileName);
        assertTrue(Files.exists(savedFile));
        byte[] savedData = Files.readAllBytes(savedFile);
        assertArrayEquals("test image content".getBytes(), savedData);
    }

    @Test
    @DisplayName("Deve criar diretório se não existir ao salvar imagem")
    void testSaveImageFile_CreatesDirectoryIfNotExists() throws IOException {
        // Arrange
        Path newDir = tempDir.resolve("new-directory");
        FoodItemConfig.ImageConfig imageConfig = new FoodItemConfig.ImageConfig();
        imageConfig.setDirectory(newDir + "/");
        foodItemConfig.setImage(imageConfig);
        storage = new FoodItemImageStorageImpl(foodItemConfig);

        String fileName = "test.jpg";
        String base64Data = Base64.getEncoder().encodeToString("content".getBytes());

        // Act
        storage.saveImageFile(base64Data, fileName);

        // Assert
        assertTrue(Files.exists(newDir));
        assertTrue(Files.exists(newDir.resolve(fileName)));
    }

    @Test
    @DisplayName("Deve salvar múltiplas imagens")
    void testSaveImagesFiles_Success() {
        // Arrange
        List<FoodItemImageDTO> images = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            FoodItemImageDTO image = new FoodItemImageDTO();
            image.setFileName("image_" + i + ".jpg");
            image.setData(Base64.getEncoder().encodeToString(("content " + i).getBytes()));
            images.add(image);
        }

        // Act
        storage.saveImagesFiles(images);

        // Assert
        for (int i = 1; i <= 3; i++) {
            Path savedFile = tempDir.resolve("image_" + i + ".jpg");
            assertTrue(Files.exists(savedFile), "File image_" + i + ".jpg should exist");
        }
    }

    @Test
    @DisplayName("Deve retornar dados de imagem existente em base64")
    void testGetImageData_Success() throws IOException {
        // Arrange
        String fileName = "existing-image.jpg";
        String originalContent = "test image data";
        Path imagePath = tempDir.resolve(fileName);
        Files.write(imagePath, originalContent.getBytes());

        // Act
        String retrievedData = storage.getImgaeData(fileName);

        // Assert
        assertNotNull(retrievedData);
        String decodedData = new String(Base64.getDecoder().decode(retrievedData));
        assertEquals(originalContent, decodedData);
    }

    @Test
    @DisplayName("Deve lançar FoodItemException quando imagem não existe")
    void testGetImageData_FileNotFound() {
        // Arrange
        String nonExistentFile = "non-existent.jpg";

        // Act & Assert
        FoodItemException exception = assertThrows(FoodItemException.class,
            () -> storage.getImgaeData(nonExistentFile));
        assertEquals(404, exception.getCode());
    }

    @Test
    @DisplayName("Deve recuperar dados de múltiplas imagens")
    void testGetImagesFiles_Success() throws IOException {
        // Arrange
        List<FoodItemImageDTO> images = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            FoodItemImageDTO image = new FoodItemImageDTO();
            String fileName = "image_" + i + ".jpg";
            image.setFileName(fileName);
            images.add(image);

            // Create the actual files
            Path imagePath = tempDir.resolve(fileName);
            Files.write(imagePath, ("content " + i).getBytes());
        }

        // Act
        storage.getImagesFiles(images);

        // Assert
        for (int i = 0; i < images.size(); i++) {
            FoodItemImageDTO image = images.get(i);
            assertNotNull(image.getData());
            String decodedData = new String(Base64.getDecoder().decode(image.getData()));
            assertEquals("content " + (i + 1), decodedData);
        }
    }

    @Test
    @DisplayName("Deve deletar imagem existente")
    void testDeleteImageFile_Success() throws IOException {
        // Arrange
        String fileName = "to-delete.jpg";
        Path imagePath = tempDir.resolve(fileName);
        Files.write(imagePath, "content".getBytes());
        assertTrue(Files.exists(imagePath));

        // Act
        storage.deleteImageFile(fileName);

        // Assert
        assertFalse(Files.exists(imagePath));
    }

    @Test
    @DisplayName("Não deve lançar exceção ao deletar imagem inexistente")
    void testDeleteImageFile_FileNotFound() {
        // Arrange
        String nonExistentFile = "non-existent.jpg";

        // Act & Assert
        assertDoesNotThrow(() -> storage.deleteImageFile(nonExistentFile));
    }

    @Test
    @DisplayName("Deve deletar múltiplas imagens")
    void testDeleteImagesFiles_Success() throws IOException {
        // Arrange
        List<FoodItemImageDTO> images = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            FoodItemImageDTO image = new FoodItemImageDTO();
            String fileName = "delete_" + i + ".jpg";
            image.setFileName(fileName);
            images.add(image);

            // Create the files
            Path imagePath = tempDir.resolve(fileName);
            Files.write(imagePath, ("content " + i).getBytes());
            assertTrue(Files.exists(imagePath));
        }

        // Act
        storage.deleteImagesFiles(images);

        // Assert
        for (FoodItemImageDTO image : images) {
            Path imagePath = tempDir.resolve(image.getFileName());
            assertFalse(Files.exists(imagePath), "File " + image.getFileName() + " should be deleted");
        }
    }

    @Test
    @DisplayName("Deve manter o diretório após deletar imagem")
    void testDeleteImageFile_DirectoryRemains() throws IOException {
        // Arrange
        String fileName = "test.jpg";
        Path imagePath = tempDir.resolve(fileName);
        Files.write(imagePath, "content".getBytes());

        // Act
        storage.deleteImageFile(fileName);

        // Assert
        assertFalse(Files.exists(imagePath));
        assertTrue(Files.exists(tempDir), "Directory should still exist");
    }

    @Test
    @DisplayName("Deve lidar com lista vazia ao salvar imagens")
    void testSaveImagesFiles_EmptyList() {
        // Arrange
        List<FoodItemImageDTO> emptyList = new ArrayList<>();

        // Act & Assert
        assertDoesNotThrow(() -> storage.saveImagesFiles(emptyList));
    }

    @Test
    @DisplayName("Deve lidar com lista vazia ao deletar imagens")
    void testDeleteImagesFiles_EmptyList() {
        // Arrange
        List<FoodItemImageDTO> emptyList = new ArrayList<>();

        // Act & Assert
        assertDoesNotThrow(() -> storage.deleteImagesFiles(emptyList));
    }

    @Test
    @DisplayName("Deve sobrescrever imagem existente ao salvar com mesmo nome")
    void testSaveImageFile_OverwriteExisting() throws IOException {
        // Arrange
        String fileName = "overwrite.jpg";
        String firstContent = "first content";
        String secondContent = "second content";

        String firstBase64 = Base64.getEncoder().encodeToString(firstContent.getBytes());
        String secondBase64 = Base64.getEncoder().encodeToString(secondContent.getBytes());

        // Act
        storage.saveImageFile(firstBase64, fileName);
        storage.saveImageFile(secondBase64, fileName);

        // Assert
        Path savedFile = tempDir.resolve(fileName);
        byte[] savedData = Files.readAllBytes(savedFile);
        assertArrayEquals(secondContent.getBytes(), savedData);
    }
}


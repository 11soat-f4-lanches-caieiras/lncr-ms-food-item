package br.com.tp.lncr.fooditem.handlers;

import br.com.tp.lncr.core.exceptions.FoodItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class FoodItemInboundHandlerTest {

    private FoodItemInboundHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FoodItemInboundHandler();
    }

    @Test
    void deveManipularFoodItemExceptionComSucesso() {
        FoodItemException exception = new FoodItemException("Item de comida não encontrado", 404);

        ResponseEntity<Object> response = handler.handleFoodItemExceptionException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveManipularFoodItemExceptionComBadRequest() {
        FoodItemException exception = new FoodItemException("Dados inválidos para o item de comida", 400);

        ResponseEntity<Object> response = handler.handleFoodItemExceptionException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deveManipularFoodItemExceptionComConflict() {
        FoodItemException exception = new FoodItemException("Item de comida já existe", 409);

        ResponseEntity<Object> response = handler.handleFoodItemExceptionException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void deveManipularFoodItemExceptionComInternalServerError() {
        FoodItemException exception = new FoodItemException("Erro interno ao processar item de comida", 500);

        ResponseEntity<Object> response = handler.handleFoodItemExceptionException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}


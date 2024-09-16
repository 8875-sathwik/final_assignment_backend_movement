package io.swagger.api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    @Test
    void checkBody_NullBody_ReturnsFalseWithErrorValueAndType() {
        // Prepare
        ErrorHandler errorHandler = new ErrorHandler();
        Object body = null;
        List<String> fieldsToValidate = Arrays.asList("name", "email", "phone");

        // Execute
        boolean result = errorHandler.checkbody(body, fieldsToValidate);

        // Assert
        assertFalse(result);
        assertEquals("body", errorHandler.getErrorValue());
        assertEquals("body is null", errorHandler.getErrorType());
    }

    @Test
    void checkBody_EmptyField_ReturnsFalseWithErrorValueAndType() {
        // Prepare
        ErrorHandler errorHandler = new ErrorHandler();
        TestObject body = new TestObject("", "example@example.com", "1234567890");
        List<String> fieldsToValidate = Arrays.asList("name", "email", "phone");

        // Execute
        boolean result = errorHandler.checkbody(body, fieldsToValidate);

        // Assert
        assertFalse(result);
        assertEquals("name", errorHandler.getErrorValue());
        assertEquals("empty", errorHandler.getErrorType());
    }

    @Test
    void checkBody_InvalidEmail_ReturnsFalseWithErrorValueAndType() {
        // Prepare
        ErrorHandler errorHandler = new ErrorHandler();
        TestObject body = new TestObject("John", "invalid-email", "1234567890");
        List<String> fieldsToValidate = Arrays.asList("name", "email", "phone");

        // Execute
        boolean result = errorHandler.checkbody(body, fieldsToValidate);

        // Assert
        assertFalse(result);
        assertEquals("email", errorHandler.getErrorValue());
        assertEquals("invalid", errorHandler.getErrorType());
    }

    @Test
    void checkBody_ValidBody_ReturnsTrue() {
        // Prepare
        ErrorHandler errorHandler = new ErrorHandler();
        TestObject body = new TestObject("John", "example@example.com", "1234567890");
        List<String> fieldsToValidate = Arrays.asList("name", "email", "phone");

        // Execute
        boolean result = errorHandler.checkbody(body, fieldsToValidate);

        // Assert
        assertTrue(result);
        assertNull(errorHandler.getErrorValue());
        assertNull(errorHandler.getErrorType());
    }

    // Helper class
    private static class TestObject {
        private String name;
        private String email;
        private String phone;

        public TestObject(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }
    }
}

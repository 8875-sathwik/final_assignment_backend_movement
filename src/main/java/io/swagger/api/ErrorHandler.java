package io.swagger.api;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ErrorHandler {
    private String errorValue;
    private String errorType;

    public boolean checkbody(Object body, List<String> fieldsToValidate) {
        errorValue = null;  // Reset the errorValue variable
        errorType = null;   // Reset the errorType variable

        if (body == null) {
            errorValue = "body";
            errorType = "body is null";
            return false;
        }

        Field[] fields = body.getClass().getDeclaredFields();
        Pattern alphanumericPattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Pattern emailPattern = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.\\w{2,}$");
        Pattern phonePattern = Pattern.compile("^\\d{10}$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");


        for (Field field : fields) {
            if (fieldsToValidate.contains(field.getName())) {
                field.setAccessible(true);
                try {
                    Object value = field.get(body);
                    if (value == null) {
                        errorValue = field.getName();
                        errorType = "empty";
                        return false;
                    }
                    if (value instanceof String) {
                        String stringValue = (String) value;
                        if (stringValue.trim().isEmpty()) {
                            errorValue = field.getName();
                            errorType = "empty";
                            return false;
                        }
                        if (field.getName().equals("email")) {
                            if (!emailPattern.matcher(stringValue).matches()) {
                                errorValue = field.getName();
                                errorType = "invalid";
                                return false;
                            }
                        } else if (field.getName().equals("phone")) {
                            if (!phonePattern.matcher(stringValue).matches()) {
                                errorValue = field.getName();
                                errorType = "invalid";
                                return false;
                            }
                        } else if (field.getName().equals("password")) { // Validation for password field
                            if (!passwordPattern.matcher(stringValue).matches()) {
                                errorValue = field.getName();
                                errorType = "invalid";
                                return false;
                            }
                        } else {
                            if (!alphanumericPattern.matcher(stringValue).matches()) {
                                errorValue = field.getName();
                                errorType = "invalid";
                                return false;
                            }
                        }
                    } else if (!field.getType().isPrimitive() && !field.getType().isInstance(value)) {
                        errorValue = field.getName();
                        errorType = "invalidType";
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    errorValue = field.getName();
                    errorType = "error accessing field";
                    return false;
                }
            }
        }
        return true;
    }

    public String getErrorValue() {
        return errorValue;
    }

    public String getErrorType() {
        return errorType;
    }




}

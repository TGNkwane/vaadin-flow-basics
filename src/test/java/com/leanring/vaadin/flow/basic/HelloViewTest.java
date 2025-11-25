package com.leanring.vaadin.flow.basic;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloViewTest {

    @Test
    void shouldInitializeComponents() {
        HelloView view = new HelloView();

        TextField nameField = (TextField) view.getChildren()
                .filter(c -> c instanceof TextField)
                .findFirst()
                .orElseThrow();

        Button greetButton = (Button) view.getChildren()
                .filter(c -> c instanceof Button)
                .findFirst()
                .orElseThrow();

        Assertions.assertEquals("Your name", nameField.getLabel());
        Assertions.assertEquals("Say Hi", greetButton.getText());
    }

    @Test
    void shouldGreetUser() {
        HelloView view = new HelloView();

        TextField nameField = (TextField) view.getChildren()
                .filter(c -> c instanceof TextField)
                .findFirst()
                .orElseThrow();

        Button greetButton = (Button) view.getChildren()
                .filter(c -> c instanceof Button)
                .findFirst()
                .orElseThrow();

        Span messageSpan = (Span) view.getChildren()
                .filter(c -> c instanceof Span)
                .findFirst()
                .orElseThrow();

        nameField.setValue("Vaadin User");
        greetButton.click();

        Assertions.assertEquals("Hello Vaadin User \uD83D\uDC4B", messageSpan.getText());
    }
}

package ru.aegorova.annotations.html.creator;

@HtmlForm(method = "post", action = "/users")
public class User {
    @HtmlInput(type = "text", name = "first_name", placeholder = "Имя")
    private String firstName;
    @HtmlInput(type = "text", name = "last_name", placeholder = "Фамилия")
    private String lastName;
    @HtmlInput(type = "email", name = "email", placeholder = "Email")
    private String email;
    @HtmlInput(type = "password", name = "password", placeholder = "Пароль")
    private String password;
}

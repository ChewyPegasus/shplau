package by.shplau.dto.responses;

import by.shplau.entities.User;

public class UserProfileResponse {
    private String username;
    private String email;

    public UserProfileResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // Геттеры
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

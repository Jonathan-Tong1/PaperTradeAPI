package com.jt.securetrading.dtos;

import com.jt.securetrading.models.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO {

    private Long userId;
    private String userName;
    private String email;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;
    private String twoFactorSecret;
    private boolean isTwoFactorEnabled;
    private String signUpMethod;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public UserDTO() {
    }

    public UserDTO(Long userId, String userName, String email, boolean accountNonLocked, boolean accountNonExpired, boolean credentialsNonExpired, boolean enabled, LocalDate credentialsExpiryDate, LocalDate accountExpiryDate, String twoFactorSecret, boolean isTwoFactorEnabled, String signUpMethod, Role role, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.credentialsExpiryDate = credentialsExpiryDate;
        this.accountExpiryDate = accountExpiryDate;
        this.twoFactorSecret = twoFactorSecret;
        this.isTwoFactorEnabled = isTwoFactorEnabled;
        this.signUpMethod = signUpMethod;
        this.role = role;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDate getCredentialsExpiryDate() {
        return credentialsExpiryDate;
    }

    public void setCredentialsExpiryDate(LocalDate credentialsExpiryDate) {
        this.credentialsExpiryDate = credentialsExpiryDate;
    }

    public LocalDate getAccountExpiryDate() {
        return accountExpiryDate;
    }

    public void setAccountExpiryDate(LocalDate accountExpiryDate) {
        this.accountExpiryDate = accountExpiryDate;
    }

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public boolean isTwoFactorEnabled() {
        return isTwoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        isTwoFactorEnabled = twoFactorEnabled;
    }

    public String getSignUpMethod() {
        return signUpMethod;
    }

    public void setSignUpMethod(String signUpMethod) {
        this.signUpMethod = signUpMethod;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", accountNonLocked=" + accountNonLocked +
                ", accountNonExpired=" + accountNonExpired +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", credentialsExpiryDate=" + credentialsExpiryDate +
                ", accountExpiryDate=" + accountExpiryDate +
                ", twoFactorSecret='" + twoFactorSecret + '\'' +
                ", isTwoFactorEnabled=" + isTwoFactorEnabled +
                ", signUpMethod='" + signUpMethod + '\'' +
                ", role=" + role +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return accountNonLocked == userDTO.accountNonLocked && accountNonExpired == userDTO.accountNonExpired && credentialsNonExpired == userDTO.credentialsNonExpired && enabled == userDTO.enabled && isTwoFactorEnabled == userDTO.isTwoFactorEnabled && Objects.equals(userId, userDTO.userId) && Objects.equals(userName, userDTO.userName) && Objects.equals(email, userDTO.email) && Objects.equals(credentialsExpiryDate, userDTO.credentialsExpiryDate) && Objects.equals(accountExpiryDate, userDTO.accountExpiryDate) && Objects.equals(twoFactorSecret, userDTO.twoFactorSecret) && Objects.equals(signUpMethod, userDTO.signUpMethod) && Objects.equals(role, userDTO.role) && Objects.equals(createdDate, userDTO.createdDate) && Objects.equals(updatedDate, userDTO.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, email, accountNonLocked, accountNonExpired, credentialsNonExpired, enabled, credentialsExpiryDate, accountExpiryDate, twoFactorSecret, isTwoFactorEnabled, signUpMethod, role, createdDate, updatedDate);
    }
}

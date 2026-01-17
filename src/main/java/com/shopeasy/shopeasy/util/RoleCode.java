package com.shopeasy.shopeasy.util;

import lombok.Getter;

@Getter
public enum RoleCode {
    ADMIN("Administrateur", "Accès complet à toutes les fonctionnalités"),
    MANAGER("Gestionnaire", "Gestion des produits, catégories et utilisateurs"),
    CUSTOMER("Client", "Accès client standard avec fonctionnalités d'achat"),
    SUPPORT("Support", "Accès au support client et gestion des tickets");

    private final String displayName;
    private final String description;

    RoleCode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Vérifie si le code donné correspond à un rôle valide
     */
    public static boolean isValid(String code) {
        if (code == null) return false;
        try {
            RoleCode.valueOf(code.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Récupère un RoleCode depuis une chaîne (case insensitive)
     */
    public static RoleCode fromString(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Le code de rôle ne peut pas être null");
        }
        try {
            return RoleCode.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Code de rôle invalide: " + code);
        }
    }

    /**
     * Vérifie si ce rôle a un niveau d'autorisation supérieur ou égal à un autre
     */
    public boolean hasAuthorityLevel(RoleCode other) {
        return this.ordinal() <= other.ordinal();
    }
}

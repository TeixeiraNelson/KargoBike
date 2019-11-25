package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class RoleEntity {
    String role;
    String roleId;

    public RoleEntity(){}

    public RoleEntity(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getRoleId() { return roleId; }

    public void setRoleId(String roleId) { this.roleId = roleId; }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roleId", roleId);
        result.put("role", role);

        return result;
    }
}

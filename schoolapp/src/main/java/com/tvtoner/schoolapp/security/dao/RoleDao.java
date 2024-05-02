package com.tvtoner.schoolapp.security.dao;

import com.tvtoner.schoolapp.security.entity.Role;

public interface RoleDao {

    public Role findRoleByName(String theRoleName);

}

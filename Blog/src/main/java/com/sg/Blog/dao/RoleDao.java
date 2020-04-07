/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.dao;

import com.sg.Blog.entity.Role;
import com.sg.Blog.entity.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mohamed
 */
@Repository
public interface RoleDao extends JpaRepository<Role, Integer>{
    List<Role> getAllRoles();
    Role addRole(Role role);
    Role getRoleById(int id);
    void updateRole(Role role);
    void deleteRole(int id);    
    Set<Role> getAllRolesforUser(User user);
}

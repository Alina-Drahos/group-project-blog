/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.controller;

import com.sg.Blog.dao.RoleDao;
import com.sg.Blog.dao.UserDao;
import com.sg.Blog.entity.Role;
import com.sg.Blog.entity.User;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author carlo
 */
@Controller
public class AdminController {

    @Autowired
    UserDao users;

    @Autowired
    RoleDao roles;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        model.addAttribute("users", users.getAllUsers());
        return "admin";
    }

    @PostMapping("/addUser")
    public String addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEnabled(true);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roles.getRoleByRole("ROLE_USER"));
        user.setRoles(userRoles);

        users.createUser(user);

        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(Integer id) {
        users.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/editUser")
    public String editUserDisplay(Model model, Integer id) {
        User user = users.getUserById(id);
        List<Role> oleList = roles.getAllRoles(r);

        model.addAttribute("user", user);
        model.addAttribute("roles", roleList);
        return "editUser";
    }

    @PostMapping(value = "/editUser")
    public String editUserAction(String[] roleIdList, Boolean enabled, Integer id) {
        User user = users.getUserById(id);
        if (enabled != null) {
            user.setEnabled(enabled);
        } else {
            user.setEnabled(false);
        }

        Set<Role> roleList = new HashSet<>();
        for (String roleId : roleIdList) {
            Role role = roles.getRoleById(Integer.parseInt(roleId));
            roleList.add(role);
        }
        user.setRoles(roleList);
        users.updateUser(user);

        return "redirect:/admin";
    }

    @PostMapping("editPassword")
    public String editPassword(Integer id, String password, String confirmPassword) {
        User user = users.getUserById(id);

        if (password.equals(confirmPassword)) {
            user.setPassword(encoder.encode(password));
            users.updateUser(user);
            return "redirect:/admin";
        } else {
            return "redirect:/editUser?id=" + id + "&error=1";
        }
    }

    @GetMapping("/editUser")
    public String editUserDisplay(Model model, Integer id, Integer error) {
        User user = users.getUserById(id);
        List roleList = roles.getAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roleList);

        if (error != null) {
            if (error == 1) {
                model.addAttribute("error", "Passwords did not match, password was not updated.");
            }
        }

        return "editUser";
    }

}

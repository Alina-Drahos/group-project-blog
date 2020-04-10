/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.controller;

import com.sg.Blog.dao.ContentDao;
import com.sg.Blog.dao.RoleDao;
import com.sg.Blog.dao.UserDao;
import com.sg.Blog.entity.Content;
import com.sg.Blog.entity.Role;
import com.sg.Blog.entity.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.Validate;

/**
 *
 *
 */
@Controller
public class userController {

    @Autowired
    UserDao users;

    @Autowired
    RoleDao roles;

    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    ContentDao contentDao;

    Set<ConstraintViolation<User>> violations = new HashSet<>();

    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        List<Content> staticPages = contentDao.findAllByIsStatic(true);
        List<Content> pages = new ArrayList<>();
        for (Content p : staticPages) {
            if (!p.getPageName().equalsIgnoreCase("home")) {
                pages.add(p);
            }
        }
        
        model.addAttribute("pages", pages);
        model.addAttribute("users", users.findAll());
        model.addAttribute("errors", violations);
        return "admin";
    }

    @PostMapping("/addUser")
    public String addUser(String username, String password) {
        List<User> allUsers = users.findAll();
        List<String> usernames = new ArrayList<>();
        for (User currentUser : allUsers) {
            usernames.add(currentUser.getUsername());
        }
        if (usernames.contains(username)) {
            //return error that username already exists.
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.setEnabled(true);

            user.setRoles(roles.findAllByRole("ROLE_USER"));

            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            violations = validate.validate(user);

            if (violations.isEmpty()) {
                users.save(user);
            }
        }
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(Integer id) {
        users.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "/editUser")
    public String editUserAction(String[] roleIdList, Boolean enabled, Integer id) {
        User user = users.findById(id).orElse(null);
        if (enabled != null) {
            user.setEnabled(enabled);
        } else {
            user.setEnabled(false);
        }
        Set<Role> roleList = new HashSet<>();
        if (roleIdList != null) {
            for (String roleId : roleIdList) {
                Role role = roles.findById(Integer.parseInt(roleId)).orElse(null);
                roleList.add(role);
            }
        }
        user.setRoles(roleList);
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);
        if (violations.isEmpty()) {
            users.save(user);
        }

        return "redirect:/admin";
    }

    @PostMapping("editPassword")
    public String editPassword(Integer id, String password, String confirmPassword) {
        User user = users.findById(id).orElse(null);

        if (password.equals(confirmPassword)) {
            user.setPassword(encoder.encode(password));
            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            violations = validate.validate(user);
            if (violations.isEmpty()) {
                users.save(user);
            }
            return "redirect:/admin";
        } else {
            return "redirect:/editUser?id=" + id + "&error=1";
        }
    }

    @GetMapping("/editUser")
    public String editUserDisplay(Model model, Integer id, Integer error) {
        User user = users.findById(id).orElse(null);
        List roleList = roles.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roleList);
        model.addAttribute("errors", violations);

        if (error != null) {
            if (error == 1) {
                model.addAttribute("error", "Passwords did not match, password was not updated.");
            }
        }

        return "editUser";
    }

}
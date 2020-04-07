/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.controller;

import com.sg.Blog.dao.ContentDao;
import com.sg.Blog.dao.RoleDao;
import com.sg.Blog.dao.TagDao;
import com.sg.Blog.dao.UserDao;
import com.sg.Blog.entity.Role;
import com.sg.Blog.entity.User;
import java.util.ArrayList;
import java.util.Base64.Encoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author alinc
 */
@Controller
public class UserController {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    ContentDao contentDao;

    @Autowired
    TagDao tagDao;

    @GetMapping("users")
    public String displayUsers(Model model) {
        List<User> users = userDao.findAll();
        List<Role> userRoles = roleDao.findAll();
        model.addAttribute("users", users);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("user", new User());
        return "users";

    }

    @PostMapping("/addUser")
    public String addUser(@Valid User user, BindingResult result, HttpServletRequest request, Model model) {
        String[] roleIds = request.getParameterValues("roleId");
        String password = request.getParameter("password");

        Set<Role> userRoles = new HashSet<>();
        if (roleIds != null) {
            for (String roleId : roleIds) {
                userRoles.add(roleDao.findById(Integer.parseInt(roleId)).orElse(null));
            }
        } else {
            FieldError error = new FieldError("user", "userRoles", "Must include one role");
            result.addError(error);
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDao.findAll());
            model.addAttribute("roles", roleDao.findAll());
            return "/admin";
        }

        user.setRoles(userRoles);
        user.setPassword(encoder.encode(password));
        userDao.save(user);
        return "redirect:/admin";
    }

    @GetMapping("deleteUser")
    public String deleteUSer(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        userDao.deleteById(id);
        return "redirect:/admin";

    }

//    @GetMapping("editUser")
//    public String editUser(HttpServletRequest request, Model model) {
//        int id = Integer.parseInt(request.getParameter("id"));
//        User user = userDao.findById(id).orElse(null);
//        user.setRoles(roleDao.getAllRolesforUser(user));
//        List<Role> userRoles = roleDao.findAll();
//
//        model.addAttribute("user", user);
//        model.addAttribute("userRoles", userRoles);
//        return "user";
//
//    }

    @PostMapping("editUser")
    public String performEditUser(@Valid User user, BindingResult result, HttpServletRequest request,
            Model model) {

        String[] roleIds = request.getParameterValues("roleId");
         String password = request.getParameter("password");

        Set<Role> userRoles = new HashSet<>();
        if (roleIds != null) {
            for (String roleId : roleIds) {
                userRoles.add(roleDao.findById(Integer.parseInt(roleId)).orElse(null));
            }
        } else {
            FieldError error = new FieldError("user", "userRoles", "Must include one role");
            result.addError(error);
        }

        user.setRoles(userRoles);

        if (result.hasErrors()) {
            model.addAttribute("user", userDao.findAll());
            model.addAttribute("roles", roleDao.findAll());
            return "/admin";
        }

        user.setPassword(encoder.encode(password));
        userDao.save(user);
        return "redirect:/admin";

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.controller;

import com.sg.Blog.dao.ContentDao;
import com.sg.Blog.dao.UserDao;
import com.sg.Blog.entity.Content;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author mohamed
 */
@Controller
public class ContentController {
    
    @Autowired
    ContentDao contentDao;
    
    @Autowired
    UserDao userDao;
    
    @GetMapping("/")
    public String displayHome(Model model) {
        Content disclaimer = new Content();
        List<Content> contents = contentDao.findAllByIsStatic(true);
        
        for(Content c : contents) {
            if (c.getTitle().equalsIgnoreCase("disclaimer")) {
                disclaimer = c;
            }
        }
        model.addAttribute("home", disclaimer);
        return "index";
    }
    
    @GetMapping("contents")
    public String displayContent(Model model) {
        List<Content> contents = contentDao.findAllByIsStatic(false);
        model.addAttribute("contents", contents);
        
        return "contents";
    }
    
    @GetMapping("addPost")
    public String getPost(Model model) {
        model.addAttribute("content", new Content());
        return "addPost";
    }
    
    @PostMapping("addPost")
    public String postContent(Principal p, Content content) {
        content.setUser(userDao.findByUsername(p.getName()));
        contentDao.save(content);
        return "redirect:/contents";
    }
    
    @GetMapping("deletePost")
    public String deletePost(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        contentDao.deleteById(id);
        
        return "redirect:/contents";
    }
    
    @GetMapping("editPost")
    public String editPost(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Content c = contentDao.findById(id).orElse(null);
        model.addAttribute("content", c);
        
        return "editPost";
    }
    
    @PostMapping("editPost")
    public String performEdit(Principal p, Content content) {
        content.setUser(userDao.findByUsername(p.getName()));
        contentDao.save(content);
        
        return "redirect:/contents";
    }
    
    @GetMapping("addPage")
    public String getPage(Model model) {
        model.addAttribute("page", new Content());
        return "addPage";
    }
    
    @PostMapping("addPage")
    public String addPage(Principal p, Content page) {
        page.setUser(userDao.findByUsername(p.getName()));
        contentDao.save(page);
        return "redirect:/";
    }
}

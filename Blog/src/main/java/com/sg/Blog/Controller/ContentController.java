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
import com.sg.Blog.entity.Content;
import com.sg.Blog.entity.Tag;
import com.sg.Blog.entity.User;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    TagDao tagDao;

    @Autowired
    RoleDao roleDao;

    @GetMapping("/")
    public String displayHome(Model model) {
        Content homePage = new Content();
        List<Content> contents = contentDao.findAllByIsStatic(true);
        List<Content> pages = new ArrayList<>();

        for (Content c : contents) {
            if (c.getPageName().equalsIgnoreCase("home")) {
                homePage = c;
            }
        }

        for (Content p : contents) {
            if (!p.getPageName().equalsIgnoreCase("home")) {
                pages.add(p);
            }
        }
        model.addAttribute("pages", pages);
        model.addAttribute("home", homePage);
        return "index";
    }

    @GetMapping("page/{id}")
    public String setUpPage(@PathVariable Integer id, Model model) {
        List<Content> allPages = contentDao.findAllByIsStatic(true);
        Content homePage = new Content();
        Content page = contentDao.findById(id).orElse(null);
        List<Content> pages = new ArrayList<>();

        for (Content p : allPages) {
            if (p.getPageName().equalsIgnoreCase("home")) {
                homePage = p;
            }
        }

        for (Content p : allPages) {
            if (!p.getPageName().equalsIgnoreCase("home")) {
                pages.add(p);
            }
        }

        model.addAttribute("pages", pages);
        model.addAttribute("home", homePage);
        model.addAttribute("page", page);
        return "staticPages";
    }

    @GetMapping("contents")
    public String displayContent(Model model) {
        List<Content> staticPages = contentDao.findAllByIsStatic(true);
        List<Content> pages = new ArrayList<>();

        for (Content p : staticPages) {
            if (!p.getPageName().equalsIgnoreCase("home")) {
                pages.add(p);
            }
        }

        List<Content> approvedPosts = contentDao.findAllByApproved(true);

        model.addAttribute("contents", approvedPosts);
        model.addAttribute("pages", pages);

        return "contents";
    }

    @GetMapping("addPost")
    public String getPost(Model model) {
        model.addAttribute("content", new Content());
        model.addAttribute("today", LocalDate.now());
        return "addPost";
    }

    @PostMapping("addPost")
    public String postContent(@Valid Content content, BindingResult result, Model model, Principal p) {
        if (result.hasErrors()) {
            return "addPost";
        }

        String[] parts = content.getBody().split(" ");
        Set<Tag> tags = new HashSet<>();

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("<p>") || parts[i].contains("</p>")) {
                parts[i] = parts[i].replace("<p>", "");
                parts[i] = parts[i].replace("</p>", "");
            }

            if (parts[i].contains("#")) {
                if (tagDao.findByHashtag(parts[i]) == null) {
                    Tag tag = new Tag();
                    tag.setHashtag(parts[i]);
                    tagDao.save(tag);
                    tags.add(tag);
                } else {
                    tags.add(tagDao.findByHashtag(parts[i]));
                }
            }
        }

        User user = userDao.findByUsername(p.getName());
        content.setHashtags(tags);
        content.setUser(user);
        if (user.getRoles().contains(roleDao.findByRole("ROLE_ADMIN"))) {
            content.setApproved(true);
        }
        content.setIsStatic(false);
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
    public String performEdit(@Valid Content content, BindingResult result, Principal p) {
        if (result.hasErrors()) {
            return "editPost";
        }

        String[] parts = content.getBody().split(" ");
        Set<Tag> tags = new HashSet<>();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("#")) {
                if (tagDao.findByHashtag(parts[i]) == null) {
                    Tag tag = new Tag();
                    tag.setHashtag(parts[i]);
                    tagDao.save(tag);
                    tags.add(tag);
                } else {
                    tags.add(tagDao.findByHashtag(parts[i]));
                }
            }
        }

        content.setHashtags(tags);
        content.setUser(userDao.findByUsername(p.getName()));
        contentDao.save(content);

        return "redirect:/contents";
    }

    @GetMapping("addPage")
    public String getPage(Model model) {
        model.addAttribute("content", new Content());
        return "addPage";
    }

    @PostMapping("addPage")
    public String addPage(Principal p, Content page, BindingResult result, Model model) {
        for (Content c : contentDao.findAllByIsStatic(true)) {
            if (c.getPageName().equalsIgnoreCase(page.getPageName())) {
                FieldError error = new FieldError("content", "pageName", "That Page Already Exist");
                result.addError(error);
                model.addAttribute("content", page);
                return "addPage";
            }
        }
        page.setUser(userDao.findByUsername(p.getName()));
        page.setIsStatic(true);
        page.setApproved(false);
        contentDao.save(page);
        return "redirect:/";
    }

    @GetMapping("/postApproval")
    public String approvedPosts(Model model) {
        List<Content> allPosts = contentDao.findAllByApproved(false);
        List<Content> staticPages = contentDao.findAllByIsStatic(true);

        List<Content> notApproved = new ArrayList<>();
        for (Content c : allPosts) {
            if (c.isIsStatic() == false) {
                notApproved.add(c);
            }
        }

        List<Content> pages = new ArrayList<>();
        for (Content p : staticPages) {
            if (!p.getPageName().equalsIgnoreCase("home")) {
                pages.add(p);
            }
        }

        model.addAttribute("pages", pages);
        model.addAttribute("posts", notApproved);
        return "postApproval";
    }

    @PostMapping("/postApproval")
    public String sendPost(Integer id) {
        Content content = contentDao.findById(id).orElse(null);
        content.setApproved(true);
        contentDao.save(content);
        return "redirect:/contents";
    }

}

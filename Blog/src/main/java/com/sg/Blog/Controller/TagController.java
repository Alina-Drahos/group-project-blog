/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.controller;

import com.sg.Blog.dao.ContentDao;
import com.sg.Blog.dao.TagDao;
import com.sg.Blog.entity.Content;
import com.sg.Blog.entity.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author mohamed
 */
@Controller
public class TagController {

    @Autowired
    TagDao tagDao;

    @Autowired
    ContentDao contentDao;

    @GetMapping("contents/hashtag")
    public String searchHashtags(String hashtag, Model model) {
        if (hashtag.isBlank()) {
            return "redirect:/contents";
        }
        
        List<Content> posts = new ArrayList<>();

        if (tagDao.findByHashtag(hashtag) != null) {
            Tag tag = tagDao.findByHashtag(hashtag);
            posts = contentDao.findAllByHashtagsContaining(tag);
        }

        List<Content> approvedPosts = new ArrayList<>();
        for (Content c : posts) {
            if (c.isApproved() == true) {
                approvedPosts.add(c);
            }
        }

        List<Content> staticPages = contentDao.findAllByIsStatic(true);
        List<Content> pages = new ArrayList<>();
        for (Content p : staticPages) {
            if (!p.getPageName().equalsIgnoreCase("home")) {
                pages.add(p);
            }
        }

        model.addAttribute("pages", pages);
        model.addAttribute("posts", approvedPosts);
        return "hashtags";
    }
}

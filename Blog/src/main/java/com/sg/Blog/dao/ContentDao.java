/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.dao;

import com.sg.Blog.entity.Content;
import com.sg.Blog.entity.Tag;
import com.sg.Blog.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mohamed
 */
@Repository
public interface ContentDao extends JpaRepository<Content, Integer>{
    List<Content> findAllByHashtagsContaining(Tag tag);
    List<Content> findAllByUser(User user);
    Content findByTitle(String title);
    
    List<Content> findAllByIsStatic(Boolean bool);
    List<Content> findAllByApproved(Boolean bool);
}

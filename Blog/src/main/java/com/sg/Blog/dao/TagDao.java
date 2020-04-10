/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.dao;

import com.sg.Blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mohamed
 */
@Repository
public interface TagDao extends JpaRepository<Tag, Integer>{
    Tag findByHashtag(String hashtag);
}

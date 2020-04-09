/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.Blog.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author mohamed
 */
@Entity
public class Content {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;
    
    @Column(name = "pagename")
    private String pageName;
    
    @NotBlank(message = "Must Enter a Title")
    @Column(nullable = false)
    private String title;
    
    @NotBlank(message = "Cannot have empty Body")
    @Column(nullable = false)
    private String body;
    
    @Column(nullable = false)
    private boolean approved;
    
    @Column(nullable = false, name = "isstatic")
    private boolean isStatic;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "datepublished")
    private LocalDate datePublished;
    
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;
    
    @ManyToMany
    @JoinTable(name = "content_tag",
            joinColumns = {@JoinColumn(name="id_content")},
            inverseJoinColumns={@JoinColumn(name="id_tag")})
    private Set<Tag> hashtags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public LocalDate getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDate datePublished) {
        this.datePublished = datePublished;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Tag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Tag> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.pageName);
        hash = 71 * hash + Objects.hashCode(this.title);
        hash = 71 * hash + Objects.hashCode(this.body);
        hash = 71 * hash + (this.approved ? 1 : 0);
        hash = 71 * hash + (this.isStatic ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.datePublished);
        hash = 71 * hash + Objects.hashCode(this.user);
        hash = 71 * hash + Objects.hashCode(this.hashtags);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Content other = (Content) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.approved != other.approved) {
            return false;
        }
        if (this.isStatic != other.isStatic) {
            return false;
        }
        if (!Objects.equals(this.pageName, other.pageName)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.datePublished, other.datePublished)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.hashtags, other.hashtags)) {
            return false;
        }
        return true;
    }
    
}

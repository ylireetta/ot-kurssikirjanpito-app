/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.User;
import java.util.List;

/**
 *
 * @author ylireett
 */
public interface UserDao {
    User createUser(User user) throws Exception;
    User findByUsername(String username);
    List<User> getAll();
    
}

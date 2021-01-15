/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.User;
import entities.UserInfo;
import java.util.List;

/**
 *
 * @author marcg
 */
public class UserDTO {

    private String username;
    private String password;
    private List<String> roles;
    private String fname;

    public UserDTO(String name, String password, List<String> roles, UserInfo fname ) {
        this.username = name;
        this.roles = roles;
        this.password = password;
        
    }

    public UserDTO(User user) {
        this.username = user.getUserName();
        this.roles = user.getRolesAsStrings();
        this.fname = user.getUserinfo().getName();
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public String getName() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }
}

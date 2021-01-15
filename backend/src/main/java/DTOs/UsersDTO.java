/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcus
 */
public class UsersDTO {
    private List<UserDTO> all = new ArrayList<>();
    

    public UsersDTO(List<User> userList) {
        for (User user : userList) {
            all.add(new UserDTO(user));
        }
    }

    public List<UserDTO> getAll() {
        return all;
    }
    
    
}

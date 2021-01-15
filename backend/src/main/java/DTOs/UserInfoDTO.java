/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.UserInfo;

/**
 *
 * @author Marcus
 */
public class UserInfoDTO {
    private String fName;

    public UserInfoDTO(String fName) {
        this.fName = fName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
    
    
    
}

package utils;


import DTOs.UserInfoDTO;
import entities.Role;
import entities.User;
import entities.UserInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    User user = new User("user", "test2");
    User admin = new User("admin", "test2");
    User both = new User("user_admin", "test2");
   
    

    if(admin.getUserPass().equals("test")||user.getUserPass().equals("test")||both.getUserPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();

    
    UserInfo userInfo = new UserInfo().setInfo(new UserInfoDTO("per"));
    UserInfo userInfo2 = new UserInfo().setInfo(new UserInfoDTO("Jurgen"));
    UserInfo userInfo3 = new UserInfo().setInfo(new UserInfoDTO("mogens"));
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    
    
    user.addRole(userRole);
    admin.addRole(adminRole);
    both.addRole(userRole);
    both.addRole(adminRole);
    user.setUserinfo(userInfo);
    admin.setUserinfo(userInfo2);
    both.setUserinfo(userInfo3);
    
    em.persist(userRole);
    em.persist(adminRole);
    em.persist(userInfo);
    em.persist(userInfo2);
    em.persist(userInfo3);
    em.persist(user);
    em.persist(admin);
    em.persist(both);
    em.getTransaction().commit();
    System.out.println("PW: " + user.getUserPass());
    System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
    System.out.println("Testing user with wrong password: " + user.verifyPassword("test2"));
    System.out.println("Created TEST Users");
   
  }

}

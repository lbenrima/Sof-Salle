package tn.sofrecom.mdrissi.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tn.sofrecom.mdrissi.entities.User;
import tn.sofrecom.mdrissi.services.LDAP.LdapService;
import tn.sofrecom.mdrissi.services.user.UserService;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {
	
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private LdapService ldapService;
	 
	 
	 @PostMapping(value="/login")
	    public String login(@RequestBody Map<String, String> json) throws ServletException, NamingException, IOException {
	        if(json.get("username") == null || json.get("password") ==null) {
	            throw new ServletException("Please fill in username and password");
	        }
	        String userMail = json.get("username");
	        String password = json.get("password");

	        /*
	         * traitement
	         */


	        	        
	        ldapService.initiate();
	        String result = ldapService.authenticate(userMail, password);
	        //String result = "Yes";
	       // System.out.println("******"+result);
	        if(result.equalsIgnoreCase("No")||result.isEmpty()) {
	        	throw new ServletException("Verify yor login and password");
	        }
	         
	         return Jwts.builder().setSubject(userMail).claim("roles", "user").setIssuedAt(new Date())
		                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
	         
	         
	        //return userMail+'-'+password;
	    }

	 	@RequestMapping("/users")
	    public List<User> findAllUsers(@RequestHeader(value="Authorization") String Authorization,@RequestHeader(value="email") String email) {
//	 		System.out.println("Authorization:"+Authorization);
//	 		System.out.println("email:"+email);
	 		String name=email.substring(0, email.indexOf("."));
	 		String prenom=email.substring(email.indexOf(".")+1, email.indexOf("@"));
	 		User user=new User();
	 		user.setMail(email);
	 		user.setName(name+" "+prenom.toUpperCase());
	 		List<User> listUsers=new ArrayList<>();
	 		listUsers.add(userService.findUser(user));
	        return listUsers;
	    }
}

package tn.sofrecom.mdrissi.services.LDAP;

import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LdapService{
	
	private static final Logger LOG = Logger.getLogger(LdapService.class.getName());

	@Value("${initial_ctx}")
	private String initial_ctx;
	
	@Value("${provider_url}")
	private String provider_url;
	
	@Value("${security_principal}")
	private String security_principal;
	
	@Value("${security_credentials}")
	private String security_credentials;
	
	@Value("${domain_base}")
	private String domainBase;
	
	
	//required private variables   
	private Properties properties;
    private DirContext dirContext;
    private SearchControls searchCtls;
	private String[] returnAttributes = { "sAMAccountName", "givenName", "cn", "mail" };
    private String baseFilter = "(&((&(objectCategory=Person)(objectClass=User)))";
	
    public void initiate() {
    	properties = new Properties();        

        properties.put(Context.INITIAL_CONTEXT_FACTORY, initial_ctx);
        properties.put(Context.PROVIDER_URL, provider_url);
        properties.put(Context.SECURITY_PRINCIPAL, security_principal);
        properties.put(Context.SECURITY_CREDENTIALS,security_credentials);
        
        //initializing active directory LDAP connection
        try {
			dirContext = new InitialDirContext(properties);
		} catch (Exception e) {
			LOG.severe(e.getMessage());
		}
        
        //initializing search controls
        searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchCtls.setReturningAttributes(returnAttributes);
    }
    
	public LdapService() {
		
		
		
	}
	
	public String authenticate(String mail, String pwd) throws NamingException {
		String resultString = "";
		 NamingEnumeration<SearchResult> result = this.searchUser(mail, "email", domainBase);
	        
	        if(result.hasMore()) {   
	        	
	        	SearchResult rs= (SearchResult)result.next();
				String distinguishedName =  rs.getNameInNamespace();
				Attributes attrs = rs.getAttributes();
				String temp = attrs.get("samaccountname").toString();
				System.out.println("Username	: " + temp.substring(temp.indexOf(":")+1));
				temp = attrs.get("givenname").toString();
				System.out.println("Name         : " + temp.substring(temp.indexOf(":")+1));
				temp = attrs.get("mail").toString();
				System.out.println("Email ID	: " + temp.substring(temp.indexOf(":")+1));
				temp = attrs.get("cn").toString();
				System.out.println("Display Name : " + temp.substring(temp.indexOf(":")+1) + "\n\n");
							
				// attempt another authentication, now with the user
	            Properties authEnv = new Properties();
	            authEnv.put(Context.INITIAL_CONTEXT_FACTORY, initial_ctx);
	            authEnv.put(Context.PROVIDER_URL, provider_url);
	            System.out.println(distinguishedName);
	            authEnv.put(Context.SECURITY_PRINCIPAL,distinguishedName);
	            authEnv.put(Context.SECURITY_CREDENTIALS, pwd);
	            
	            new InitialDirContext(authEnv) ;
	            System.out.println("Authentication successful");	
	             return resultString ="Yes";      
	          }
	        	else{
	            System.out.println("Authentication failed!! User does'nt exist");	
				resultString = "No";
			}
	        //Closing LDAP Connection
	    	this.closeLdapConnection();
	    	return resultString;
		}
	
	private String getFilter(String searchValue, String searchBy) {
    	String filter = this.baseFilter;    	
    	if(searchBy.equals("email")) {
    		filter += "(mail=" + searchValue + "))";
    	} else if(searchBy.equals("username")) {
    		filter += "(samaccountname=" + searchValue + "))";
    	}
		return filter;
	}

	public NamingEnumeration<SearchResult> searchUser(String searchValue,String searchBy, String searchBase) throws NamingException {
    	String filter = getFilter(searchValue, searchBy);    	
    	String base = (null == searchBase) ? domainBase : ("DC=sofrecom-tunisie,DC=com"); 
    	    	
		return this.dirContext.search(base, filter, this.searchCtls);
    }
	
	public void closeLdapConnection(){
        try {
            if(dirContext != null)
                dirContext.close();
        }
        catch (NamingException e) {
        	LOG.severe(e.getMessage());            
        }
    }
	
}

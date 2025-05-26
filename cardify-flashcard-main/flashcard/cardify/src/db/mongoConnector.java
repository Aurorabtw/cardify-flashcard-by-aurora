package db;

public class mongoConnector {

   
    public static boolean validateLogin(String username, String password) {

       
        return username.equals("admin") && password.equals("1234");

        
    }

}

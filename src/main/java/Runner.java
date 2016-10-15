import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class Runner {
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        System.out.println(date.getTime());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        System.out.println(sqlDate.getTime());
    }
}

import java.util.Date;

public class Runner {
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        System.out.println(date.getTime());
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        System.out.println(sqlDate.getTime());
    }
}

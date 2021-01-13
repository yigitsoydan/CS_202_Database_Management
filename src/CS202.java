import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class CS202 {
    static Scanner scanner = new Scanner(System.in);
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cs202?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            while(true) {
                System.out.println();
                System.out.print("command -> ");
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("quit")) break;
                String[] tokens = command.split(" ");
                String firstpart = tokens[0];
                String secondpart = tokens[1];

                if(firstpart.equalsIgnoreCase("show")) {
                    if(secondpart.equalsIgnoreCase("tables")) {
                        DatabaseMetaData metaData = conn.getMetaData();
                        String[] types = {"TABLE"};

                        ResultSet rs = metaData.getTables(null, null, "%", types);
                        while (rs.next()) {
                            String tableName = rs.getString(3);
                            System.out.printf("%s%n", tableName);
                        }
                    }
                }else if (firstpart.equalsIgnoreCase("query")) {
                    int querynumber = Integer.parseInt(secondpart);
                    if(querynumber == 1) {
                        stmt = conn.createStatement();

                        String sql;
                        sql = "SELECT pname, fname, flastnam FROM produces WHERE amount = (SELECT MAX(amount) from produces as f where f.pname = produces.pname)\r\n" +
                                "GROUP BY pname ORDER BY fname DESC";
                        ResultSet rs = stmt.executeQuery(sql);

                        while(rs.next()){
                            //Retrieve by column name
                            String pname = rs.getString("pname");
                            String fname = rs.getString("fname");
                            String flastnam = rs.getString("flastnam");

                            //Display values
                            System.out.print(pname);
                            System.out.print(";" + fname);
                            System.out.print(";" + flastnam);
                            System.out.println();
                        }
                        rs.close();
                        stmt.close();
                    }else if (querynumber == 2) {
                        stmt = conn.createStatement();

                        String sql;
                        sql = "SELECT DISTINCT pname, Fname, Flastname\r\n" +
                                "FROM buys\r\n" +
                                "WHERE amount = (SELECT MAX(amount) from buys as f where f.pname = buys.pname)\r\n" +
                                "GROUP BY pname\r\n" +
                                "ORDER BY Flastname ASC";
                        ResultSet rs = stmt.executeQuery(sql);

                        while(rs.next()){
                            //Retrieve by column name
                            String pname = rs.getString("pname");
                            String Fname = rs.getString("Fname");
                            String Flastname = rs.getString("Flastname");

                            //Display values
                            System.out.print(pname);
                            System.out.print(";" + Fname);
                            System.out.print(";" + Flastname);
                            System.out.println();
                        }
                        rs.close();
                        stmt.close();
                    }else if (querynumber == 3) {
                        stmt = conn.createStatement();

                        String sql;
                        sql = "SELECT Fname, Flastname\r\n" +
                                "FROM registers\r\n" +
                                "ORDER BY price*amount DESC\r\n" +
                                "LIMIT 1";
                        ResultSet rs = stmt.executeQuery(sql);

                        while(rs.next()){
                            //Retrieve by column name
                            String Fname = rs.getString("Fname");
                            String Flastname = rs.getString("Flastname");

                            //Display values
                            System.out.print(Fname);
                            System.out.print(";" + Flastname);
                            System.out.println();
                        }
                        rs.close();
                        stmt.close();
                    }else if (querynumber == 4) {
                        stmt = conn.createStatement();

                        String sql;
                        sql = "SELECT DISTINCT city, mname\r\n" +
                                "FROM markets\r\n" +
                                "WHERE budget = (SELECT MAX(budget) from markets as f where f.city = markets.city)\r\n" +
                                "GROUP BY mname";
                        ResultSet rs = stmt.executeQuery(sql);

                        while(rs.next()){
                            //Retrieve by column name
                            String city = rs.getString("city");
                            String mname = rs.getString("mname");

                            //Display values
                            System.out.print(city);
                            System.out.print(";" + mname);
                            System.out.println();
                        }
                        rs.close();
                        stmt.close();
                    }else if (querynumber == 5) {
                        stmt = conn.createStatement();

                        String sql;
                        sql = "SELECT ( SELECT COUNT(*) FROM markets ) + ( SELECT COUNT(*) FROM farmers ) AS total_users";
                        ResultSet rs = stmt.executeQuery(sql);

                        while(rs.next()){
                            //Retrieve by column name
                            String total_users = rs.getString("total_users");

                            //Display values
                            System.out.print(total_users);
                            System.out.println();
                        }
                        //STEP 6: Clean-up environment
                        rs.close();
                        stmt.close();
                    }
                }else if(secondpart.startsWith("farmer") || secondpart.startsWith("FARMER")) {
                    if(command.charAt(10) != 's') {
                        char CharAt = command.charAt(11);
                        String farmerayrilcak = command.substring(11);
                        if(CharAt == '(' ) {
                            farmerayrilcak = command.substring(12);
                        }

                        stmt = conn.createStatement();


                        String[] tokensf = farmerayrilcak.split(",");
                        tokensf[6] = tokensf[6].substring(0, tokensf[6].length()-1);
                        String zipcodeyazili = tokensf[3].trim();

                        String Name = tokensf[0];
                        String lastname = tokensf[1];
                        String Address = tokensf[2];
                        int zipcode = Integer.parseInt(zipcodeyazili);
                        String city = tokensf[4];
                        String phones = tokensf[5];
                        String emails = tokensf[6];

                        String sql = "INSERT INTO farmers " +
                                "VALUES ('"+Name+"','"+lastname+"','"+Address+"','"+zipcode+"','"+city+"','"+phones+"','"+emails+"')";
                        stmt.executeUpdate(sql);

                        System.out.print(Name);
                        System.out.print(";" + lastname);
                        System.out.print(";" + Address);
                        System.out.print(";" + zipcode);
                        System.out.print(";" + city);
                        System.out.print(";" + phones);
                        System.out.print(";" + emails);
                        System.out.println("  ADDED ON farmers TABLE.");
                    }else {
                        try {
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();

                            char CharAt = command.charAt(12);
                            String farmerayrilcak = command.substring(12);

                            if(CharAt == '(' ) {
                                farmerayrilcak = command.substring(13);
                            }


                            String[] tokensff = farmerayrilcak.split(":");

                            String farmerayrilcak1 = tokensff[0];
                            String farmerayrilcak2 = tokensff[1];

                            String[] f1 = farmerayrilcak1.split(",");
                            String[] f2 = farmerayrilcak2.split(",");

                            f1[6] = f1[6].substring(0,f1[6].length()-1);
                            f2[0] = f2[0].substring(1);
                            f2[6] = f2[6].substring(0,f2[6].length()-1);

                            String zipcodeyazili = f1[3].trim();
                            String zipcodeyazili2 = f2[3].trim();

                            String Name1 = f1[0];
                            String lastname1 = f1[1];
                            String Address1 = f1[2];
                            int zipcode1 = Integer.parseInt(zipcodeyazili);
                            String city1 = f1[4];
                            String phones1 = f1[5];
                            String emails1 = f1[6];


                            String Name2 = f2[0];
                            String lastname2 = f2[1];
                            String Address2 = f2[2];
                            int zipcode2 = Integer.parseInt(zipcodeyazili2);
                            String city2 = f2[4];
                            String phones2 = f2[5];
                            String emails2 = f2[6];



                            String sql = "INSERT INTO farmers " +
                                    "VALUES ('"+Name1+"','"+lastname1+"','"+Address1+"','"+zipcode1+"','"+city1+"','"+phones1+"','"+emails1+"')";
                            stmt.executeUpdate(sql);
                            String sql2 = "INSERT INTO farmers " +
                                    "VALUES ('"+Name2+"','"+lastname2+"','"+Address2+"','"+zipcode2+"','"+city2+"','"+phones2+"','"+emails2+"')";
                            stmt.executeUpdate(sql2);

                            conn.commit();

                            System.out.print(Name1);
                            System.out.print(";" + lastname1);
                            System.out.print(";" + Address1);
                            System.out.print(";" + zipcode1);
                            System.out.print(";" + city1);
                            System.out.print(";" + phones1);
                            System.out.print(";" + emails1);
                            System.out.println("  ADDED ON farmers TABLE.");

                            System.out.print(Name2);
                            System.out.print(";" + lastname2);
                            System.out.print(";" + Address2);
                            System.out.print(";" + zipcode2);
                            System.out.print(";" + city2);
                            System.out.print(";" + phones2);
                            System.out.print(";" + emails2);
                            System.out.println("  ADDED ON farmers TABLE.");

                        }catch(SQLException se){
                            // If there is any error.
                            conn.rollback();
                            System.out.println("None of the above INSERT statement successed and everything had rolled back");
                        }
                    }

                }else if (secondpart.startsWith("product") || secondpart.startsWith("PRODUCT")) {
                    if (command.charAt(11) != 's' && firstpart.equalsIgnoreCase("add")) {
                        char CharAt = command.charAt(12);
                        String productayrilcak = command.substring(12);
                        if(CharAt == '(' ) {
                            productayrilcak = command.substring(13);
                        }

                        stmt = conn.createStatement();


                        String[] tokensp = productayrilcak.split(",");
                        tokensp[5] = tokensp[5].substring(0, tokensp[5].length()-1);
                        String altyazili = tokensp[3].trim();
                        String mintempyazili = tokensp[4].trim();
                        String hardnessyazili = tokensp[5].trim();

                        String pname = tokensp[0];
                        String pdate = tokensp[1];
                        String hdate = tokensp[2];
                        int alt = Integer.parseInt(altyazili);
                        int mintemp = Integer.parseInt(mintempyazili);
                        int hardness = Integer.parseInt(hardnessyazili);

                        String sql = "INSERT INTO products " +
                                "VALUES ('"+pname+"','"+pdate+"','"+hdate+"','"+alt+"','"+mintemp+"','"+hardness+"')";
                        stmt.executeUpdate(sql);

                        System.out.print(pname);
                        System.out.print(";" + pdate);
                        System.out.print(";" + hdate);
                        System.out.print(";" + alt);
                        System.out.print(";" + mintemp);
                        System.out.print(";" + hardness);
                        System.out.println("  ADDED ON products TABLE.");
                    }else if (command.charAt(11) == 's' && firstpart.equalsIgnoreCase("add")) {
                        try {
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();

                            char CharAt = command.charAt(12);
                            String productayrilcak = command.substring(12);

                            if(CharAt == '(' ) {
                                productayrilcak = command.substring(13);
                            }


                            String[] tokensp = productayrilcak.split(":");

                            String pfirstpart = tokensp[0];
                            String psecondpart = tokensp[1];

                            String[] p1 = pfirstpart.split(",");
                            String[] p2 = psecondpart.split(",");

                            p1[5] = p1[5].substring(0,p1[5].length()-1);
                            p2[0] = p2[0].substring(1);
                            p2[5] = p2[5].substring(0,p2[5].length()-1);

                            String altyazili = p1[3].trim();
                            String mintempyazili = p1[4].trim();
                            String hardnessyazili = p1[5].trim();

                            String altyazili2 = p2[3].trim();
                            String mintempyazili2 = p2[4].trim();
                            String hardnessyazili2 = p2[5].trim();

                            String pname1 = p1[0];
                            String pdate1 = p1[1];
                            String hdate1 = p1[2];
                            int alt1 = Integer.parseInt(altyazili);
                            int mintemp1 = Integer.parseInt(mintempyazili);
                            int hardness1 = Integer.parseInt(hardnessyazili);

                            String pname2 = p2[0];
                            String pdate2 = p2[1];
                            String hdate2 = p2[2];
                            int alt2 = Integer.parseInt(altyazili2);
                            int mintemp2 = Integer.parseInt(mintempyazili2);
                            int hardness2 = Integer.parseInt(hardnessyazili2);

                            String sql = "INSERT INTO products " +
                                    "VALUES ('"+pname1+"','"+pdate1+"','"+hdate1+"','"+alt1+"','"+mintemp1+"','"+hardness1+"')";
                            stmt.executeUpdate(sql);

                            String sql2 = "INSERT INTO products " +
                                    "VALUES ('"+pname2+"','"+pdate2+"','"+hdate2+"','"+alt2+"','"+mintemp2+"','"+hardness2+"')";
                            stmt.executeUpdate(sql2);

                            conn.commit();

                            System.out.print(pname1);
                            System.out.print(";" + pdate1);
                            System.out.print(";" + hdate1);
                            System.out.print(";" + alt1);
                            System.out.print(";" + mintemp1);
                            System.out.print(";" + hardness1);
                            System.out.println("  ADDED ON products TABLE.");

                            System.out.print(pname2);
                            System.out.print(";" + pdate2);
                            System.out.print(";" + hdate2);
                            System.out.print(";" + alt2);
                            System.out.print(";" + mintemp2);
                            System.out.print(";" + hardness2);
                            System.out.println("  ADDED ON products TABLE.");

                        }catch(SQLException se){
                            // If there is any error.
                            conn.rollback();
                            System.out.println("None of the above INSERT statement successed and everything had rolled back");
                        }

                    }

                    else if(firstpart.equalsIgnoreCase("register")) {
                        if(command.charAt(16) != 's') {
                            char CharAt = command.charAt(17);
                            String registerayrilcak = command.substring(17);
                            if(CharAt == '(' ) {
                                registerayrilcak = command.substring(18);
                            }

                            stmt = conn.createStatement();
                            ArrayList<String> allpnames = new ArrayList<>();
                            String sql = "SELECT pname FROM products";
                            ResultSet rs = stmt.executeQuery(sql);
                            //STEP 5: Extract data from result set
                            while(rs.next()){
                                //Retrieve by column name
                                String pname = rs.getString("pname");

                                allpnames.add(pname);

                            }

                            String[] tokensr = registerayrilcak.split(",");
                            tokensr[3] = tokensr[3].substring(0, tokensr[3].length()-1);
                            String Fnamer = tokensr[0];
                            String Flastnamer = tokensr[1];
                            String pnamer = tokensr[2];
                            String IBANr = tokensr[3];
                            int amountr = 0;
                            int pricer = 0;
                            String empty = "";

                            if(allpnames.contains(pnamer)) {
                                String sql2 = "INSERT INTO registers " +
                                        "VALUES ('"+Fnamer+"','"+Flastnamer+"','"+pnamer+"','"+amountr+"','"+pricer+"','"+IBANr+"','"+empty+"','"+empty+"','"+empty+"','"+empty+"')";
                                stmt.executeUpdate(sql2);

                                System.out.print(Fnamer);
                                System.out.print(";" + Flastnamer);
                                System.out.print(";" + pnamer);
                                System.out.print(";" + IBANr);
                                System.out.println("  ADDED ON registers TABLE.");
                            } else System.out.println("Please write a product name which already exists in products table");
                        }else

                            try {
                                char CharAt = command.charAt(17);
                                String registerayrilcak = command.substring(17);
                                if(CharAt == '(' ) {
                                    registerayrilcak = command.substring(18);
                                }

                                stmt = conn.createStatement();
                                ArrayList<String> allpnames = new ArrayList<>();
                                String sql = "SELECT pname FROM products";
                                ResultSet rs = stmt.executeQuery(sql);
                                //STEP 5: Extract data from result set
                                while(rs.next()){
                                    //Retrieve by column name
                                    String pname = rs.getString("pname");

                                    allpnames.add(pname);

                                }

                                String[] tokensr = registerayrilcak.split(":");
                                String rfirstpart = tokensr[0];
                                String rsecondpart = tokensr[1];

                                String[] r1 = rfirstpart.split(",");
                                String[] r2 = rsecondpart.split(",");

                                r1[3] = r1[3].substring(0,r1[3].length()-1);
                                r2[0] = r2[0].substring(1);
                                r2[3] = r2[3].substring(0, r2[3].length()-1);

                                String Fnamer = r1[0];
                                String Flastnamer = r1[1];
                                String pnamer = r1[2];
                                String IBANr = r1[3];
                                int amountr = 0;
                                int pricer = 0;
                                String empty = "";

                                String Fnamer2 = r2[0];
                                String Flastnamer2 = r2[1];
                                String pnamer2 = r2[2];
                                String IBANr2 = r2[3];


                                String sql1 = "INSERT INTO registers " +
                                        "VALUES ('"+Fnamer+"','"+Flastnamer+"','"+pnamer+"','"+amountr+"','"+pricer+"','"+IBANr+"','"+empty+"','"+empty+"','"+empty+"','"+empty+"')";
                                stmt.executeUpdate(sql1);
                                String sql2 = "INSERT INTO registers " +
                                        "VALUES ('"+Fnamer2+"','"+Flastnamer2+"','"+pnamer2+"','"+amountr+"','"+pricer+"','"+IBANr2+"','"+empty+"','"+empty+"','"+empty+"','"+empty+"')";
                                stmt.executeUpdate(sql2);

                                conn.commit();
                                System.out.print(Fnamer);
                                System.out.print(";" + Flastnamer);
                                System.out.print(";" + pnamer);
                                System.out.print(";" + IBANr);
                                System.out.println("  ADDED ON registers TABLE.");

                                System.out.print(Fnamer2);
                                System.out.print(";" + Flastnamer2);
                                System.out.print(";" + pnamer2);
                                System.out.print(";" + IBANr2);
                                System.out.println("  ADDED ON registers TABLE.");


                            }catch(SQLException se){
                                // If there is any error.
                                conn.rollback();
                                System.out.println("None of the above INSERT statement successed and everything had rolled back");
                            }

                    }
                }else if (secondpart.startsWith("market") || secondpart.startsWith("market")) {
                    if(command.charAt(10) != 's') {
                        char CharAt = command.charAt(11);
                        String marketayrilcak = command.substring(11);
                        if(CharAt == '(' ) {
                            marketayrilcak = command.substring(12);
                        }

                        stmt = conn.createStatement();

                        String[] tokensm = marketayrilcak.split(",");
                        tokensm[5] = tokensm[5].substring(0, tokensm[5].length()-1);


                        String mzipyazili = tokensm[2].trim();
                        String mphoneyazili = tokensm[4].trim();
                        String mbudgetyazili = tokensm[5].trim();

                        String mname = tokensm[0];
                        String address = tokensm[1];
                        int zip = Integer.parseInt(mzipyazili);
                        String city = tokensm[3];
                        int phone = Integer.parseInt(mphoneyazili);
                        int budget = Integer.parseInt(mbudgetyazili);


                        String sql = "INSERT INTO markets " +
                                "VALUES ('"+mname+"','"+address+"','"+zip+"','"+city+"','"+phone+"','"+budget+"')";
                        stmt.executeUpdate(sql);

                        System.out.print(mname);
                        System.out.print(";" + address);
                        System.out.print(";" + zip);
                        System.out.print(";" + city);
                        System.out.print(";" + phone);
                        System.out.print(";" + budget);
                        System.out.println("  ADDED ON markets TABLE.");

                    }else {
                        try {
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();

                            char CharAt = command.charAt(12);
                            String marketayrilcak = command.substring(12);

                            if(CharAt == '(' ) {
                                marketayrilcak = command.substring(13);
                            }


                            String[] mm = marketayrilcak.split(":");

                            String marketayrilcak1 = mm[0];
                            String marketayrilcak2 = mm[1];

                            String[] m1 = marketayrilcak1.split(",");
                            String[] m2 = marketayrilcak2.split(",");

                            m1[5] = m1[5].substring(0,m1[5].length()-1);
                            m2[0] = m2[0].substring(1);
                            m2[5] = m2[5].substring(0,m2[5].length()-1);

                            String mzipyazili = m1[2].trim();
                            String mphoneyazili = m1[4].trim();
                            String mbudgetyazili = m1[5].trim();

                            String mzipyazili2 = m2[2].trim();
                            String mphoneyazili2 = m2[4].trim();
                            String mbudgetyazili2 = m2[5].trim();

                            String mname1 = m1[0];
                            String address1 = m1[1];
                            int zip1 = Integer.parseInt(mzipyazili);
                            String city1 = m1[3];
                            int phone1 = Integer.parseInt(mphoneyazili);
                            int budget1 = Integer.parseInt(mbudgetyazili);

                            String mname2 = m2[0];
                            String address2 = m2[1];
                            int zip2 = Integer.parseInt(mzipyazili2);
                            String city2 = m2[3];
                            int phone2 = Integer.parseInt(mphoneyazili2);
                            int budget2 = Integer.parseInt(mbudgetyazili2);


                            String sql = "INSERT INTO markets " +
                                    "VALUES ('"+mname1+"','"+address1+"','"+zip1+"','"+city1+"','"+phone1+"','"+budget1+"')";
                            stmt.executeUpdate(sql);

                            String sql2 = "INSERT INTO markets " +
                                    "VALUES ('"+mname2+"','"+address2+"','"+zip2+"','"+city2+"','"+phone2+"','"+budget2+"')";
                            stmt.executeUpdate(sql2);

                            conn.commit();

                            System.out.print(mname1);
                            System.out.print(";" + address1);
                            System.out.print(";" + zip1);
                            System.out.print(";" + city1);
                            System.out.print(";" + phone1);
                            System.out.print(";" + budget1);
                            System.out.println("  ADDED ON markets TABLE.");

                            System.out.print(mname2);
                            System.out.print(";" + address2);
                            System.out.print(";" + zip2);
                            System.out.print(";" + city2);
                            System.out.print(";" + phone2);
                            System.out.print(";" + budget2);
                            System.out.println("  ADDED ON markets TABLE.");

                        }catch(SQLException se){
                            // If there is any error.
                            conn.rollback();
                            System.out.println("None of the above INSERT statement successed and everything had rolled back");
                        }

                    }

                }


            }
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }
}


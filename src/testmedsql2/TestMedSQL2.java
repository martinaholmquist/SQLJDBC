/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testmedsql2;

import java.sql.*;
import java.util.Scanner;

public class TestMedSQL2 {

    /**
     * @param args the command line arguments
     */
    //"jdbc:mysql://localhost:3306/sti"
    // "jdbc:mysql://localhost:3306/sakila?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Ajs92ofy!");
    //?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    private static final String url = "jdbc:mysql://localhost:3306/sti?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "Ajs92ofy!";
    private static Scanner input = new Scanner(System.in);
    private static Statement sqlStatement = null;

    public static void main(String[] args) throws SQLException {

        //1. Anslut till databasen
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            System.out.println("Anslutningen lyckades!");

            sqlStatement = connection.createStatement();
            boolean exit = false;

            while (!exit) {

                System.out.println("Välj tabell");
                System.out.println("1. Kurser");
                System.out.println("2. Studenter");
                System.out.println("3. Lärare");
                System.out.println("4. Avsluta");

                //var selection = input.nextInt();  org
                int selection = input.nextInt();

                switch (selection) {
                    case 1:
                        QueryCourseTable();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        exit = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void QueryCourseTable() throws SQLException {

        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("================ COURSE ================");
            System.out.println("1. Visa alla kurser");
            System.out.println("2. Visa kurs");
            System.out.println("3. Lägg till kurs");
            System.out.println("4. Ändra kurs");
            System.out.println("5. Ta bort kurs");
            System.out.println("6. Gå tillbaka");

            //var selection = input.nextInt();org
            int selection = input.nextInt();

            input.nextLine();

            switch (selection) {
                case 1:
                    ListAllCourses();  //denna finns
                    break;
                case 2:
                    ListCourse(); //denna finns
                    break;
                case 3:
                    CreateCourse();  //denna finns
                    break;
                case 4:
                    UpdateCourse();  //denna finns
                    break;
                case 5:
                    DeleteCourse();   //denna finns
                    break;
                default:
                    exit = true;
            }

        }
    }

    private static void DeleteCourse() throws SQLException { //implementera metoden deleate
        ListAllCourses();

        System.out.println("");
        System.out.println("Vilken kurs önskar du ta bort?");
        System.out.print("Ange courseId: ");
        int courseId = input.nextInt();
        input.nextLine();
        //DELETE FROM course WHERE courseId = courseId;
        int outCome = sqlStatement.executeUpdate("DELETE FROM course WHERE courseId = " + courseId + ";  ");

        if (outCome == 1) {
            System.out.println("Kursen med courseId:" + courseId + " är nu borttagen");
        } else {
            System.out.println("Borttagning misslyckad, ange rätt ID.");
        }
    }

    private static void UpdateCourse() throws SQLException {
        boolean isInt = true;
        String change = " ";
        int courseId = 0;

        ListAllCourses();
        System.out.println("");
       // isInt = true;
       // while (isInt) {
        System.out.println("Vilken kurs önskar du ändra?");
        System.out.print("Ange courseId: ");
        courseId = input.nextInt();
        input.nextLine();
        /*int statement = sqlStatement.executeUpdate("SELECT courseId FROM course WHERE courseId = " + courseId + ";  ");
        ResultSet statement1 = sqlStatement.executeQuery("SELECT courseId FROM course WHERE courseId = " + courseId + ";  ");
        if (statement1 == 1) {
        System.out.println("Ok, Tack!");
        isInt = false;
        } else {
        System.out.println("Borttagning misslyckad, ange rätt ID.");
        }
        }*/
        

        isInt = true;
        while (isInt) {
            System.out.println("Vad önskar du ändra, name eller level?: ");
            change = input.nextLine();
            if (change.equalsIgnoreCase("name")) {
                change = "name";
                isInt = false;
            } else if (change.equalsIgnoreCase("level")) {
                change = "level";
                isInt = false;
            } else {
                System.out.println("\nSelect name or level\n");
            }
        }
        System.out.println("Ange det NYA värdet:");       
        String newInput = input.nextLine();
       
         int outCome = sqlStatement.executeUpdate("UPDATE course SET " + change + " = " +"'"+ newInput + "'" + "WHERE courseId = " + courseId + ";" );
        if (outCome == 1) {
            System.out.println("Kursen med courseId:" + courseId + " är nu ändrad enligt önskemål");
        } else {
            System.out.println("Ändring misslyckad, ange rätt ID.");
        }
    }

    private static void ListCourse() throws SQLException {
        System.out.println("Vilken kurs önskar du se? Ange det nr för det sökval som önskas"
                + "\n1: courseId"
                + "\n2: name"); 
        int choice = input.nextInt();
        input.nextLine();
        
        switch(choice){
        case 1:
        System.out.println("\nAnge kursId");
        int searchCourseId = input.nextInt();
        input.nextLine();
            System.out.println(" ");
        //utför query mot databas
        ResultSet result = sqlStatement.executeQuery("SELECT * FROM course WHERE courseId =" +searchCourseId+ ";");
        int columnCount = result.getMetaData().getColumnCount();  //anropar metoden metadata
        //hämtar alla kolumnnamn
        String[] columnNames = new String[columnCount];  //stringarray så stor som antal kolumner
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = result.getMetaData().getColumnName(i + 1);  //i+1 den är alltså inte 0-intexirad som det brukar vara....
        }
        //skriver ut kolumnerna
        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }
        //skriver ut resultatet av queryn
        while (result.next()) {  //nextmetoden frågar, finns det mer data, så länge det finns så loppar den
            System.out.println();
            for (String columnName : columnNames) {  //för varje kolumn hämtas ett värde alltså kollar kolumnen courseId ex
                String value = result.getString(columnName);  //för varje kolumn hämtas ett värde get string för det kolumnvärde på den raden  lägger på första courseid på raden1001

                if (value == null) {
                    value = "null";   //om det finns null värde så konverteras det till en string
                }
                System.out.print(PadRight(value));  //padright är för att få det snyggt vid outputten
            }
        } 
        
        break;      
        case 2:
        System.out.println("\n Ange name");
        String searchCourseName = input.nextLine();
        System.out.println(" ");
        //utför query mot databas      
        ResultSet result2 = sqlStatement.executeQuery("SELECT * FROM course WHERE name LIKE " +"'"+ searchCourseName + "%"+ "'"  + ";");
        int columnCount2 = result2.getMetaData().getColumnCount();  //anropar metoden metadata
        //hämtar alla kolumnnamn
        String[] columnNames2 = new String[columnCount2];  //stringarray så stor som antal kolumner
        for (int i = 0; i < columnCount2; i++) {
            columnNames2[i] = result2.getMetaData().getColumnName(i + 1);  //i+1 den är alltså inte 0-intexirad som det brukar vara....
        }
        //skriver ut kolumnerna
        for (String columnName : columnNames2) {
            System.out.print(PadRight(columnName));
        }
        //skriver ut resultatet av queryn
        while (result2.next()) {  //nextmetoden frågar, finns det mer data, så länge det finns så loppar den
            System.out.println();
            for (String columnName : columnNames2) {  //för varje kolumn hämtas ett värde alltså kollar kolumnen courseId ex
                String value = result2.getString(columnName);  //för varje kolumn hämtas ett värde get string för det kolumnvärde på den raden  lägger på första courseid på raden1001

                if (value == null) {
                    value = "null";   //om det finns null värde så konverteras det till en string
                }
                System.out.print(PadRight(value));  //padright är för att få det snyggt vid outputten
            }
        } 
        break; 
    }
    }

    private static void ListAllCourses() throws SQLException {
        //var result = sqlStatement.executeQuery("SELECT * FROM course;");org
        //utför query mot databas
        ResultSet result = sqlStatement.executeQuery("SELECT * FROM course;");

        //var columnCount = result.getMetaData().getColumnCount();org
        //var columnNames = new String[columnCount]; org
        int columnCount = result.getMetaData().getColumnCount();  //anropar metoden metadata
        //hämtar alla kolumnnamn
        String[] columnNames = new String[columnCount];  //stringarray så stor som antal kolumner
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = result.getMetaData().getColumnName(i + 1);  //i+1 den är alltså inte 0-intexirad som det brukar vara....
        }
        //skriver ut kolumnerna
        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }
        //skriver ut resultatet av queryn
        while (result.next()) {  //nextmetoden frågar, finns det mer data, så länge det finns så loppar den
            System.out.println();
            for (String columnName : columnNames) {  //för varje kolumn hämtas ett värde alltså kollar kolumnen courseId ex
                String value = result.getString(columnName);  //för varje kolumn hämtas ett värde get string för det kolumnvärde på den raden  lägger på första courseid på raden1001

                if (value == null) {
                    value = "null";   //om det finns null värde så konverteras det till en string
                }
                System.out.print(PadRight(value));  //padright är för att få det snyggt vid outputten
            }
        }
    }

    private static void CreateCourse() throws SQLException {
        System.out.println("Lägg till en ny kurs genom att ange namn och nivå.");
        System.out.print("Namn på kursen: ");
        //var name = input.nextLine();org
        String name = input.nextLine();
        System.out.print("Nivå på kursen: ");
        //var level = input.nextLine();org
        String level = input.nextLine();
        sqlStatement.executeUpdate("INSERT INTO course(name, level) VALUES('" + name + "', '" + level + "');");
    }

    private static String PadRight(String string) {
        //var totalStringLength = 30; org
        //var charsToPadd = totalStringLength - string.length();  org
        int totalStringLength = 30;
        int charsToPadd = totalStringLength - string.length();

        // incase the string is the same length or longer than our maximum lenght
        if (string.length() >= totalStringLength) {
            return string;
        }

        // var stringBuilder = new StringBuilder(string);org
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < charsToPadd; i++) {
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }
}

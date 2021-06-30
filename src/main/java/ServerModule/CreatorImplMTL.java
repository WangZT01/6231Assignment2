package ServerModule;

import org.omg.CORBA.ORB;
import recordFile.Record;
import recordFile.StudentRecord;
import recordFile.TeacherRecord;

import java.io.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class CreatorImplMTL extends CreatorPOA{

    File loggingFile = new File("");
    String FilePath = loggingFile.getAbsolutePath();
    ORB orb;

    HashMap<Character, ArrayList<Record>> HashMapMTL = new HashMap<Character, ArrayList<Record>>();
    int MTLcount = 0;

    public CreatorImplMTL() {
        init();
        load();
    }


    private void init() {

        loggingFile = new File( FilePath + "\\" + "LogFile" + "\\" + "MTLFile"+ "\\" + "MTLLog" +".txt");
        if(!loggingFile.exists()){
            try {
                loggingFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getCount() {

        ArrayList<Record> Recordlist = new ArrayList<>();
        int count = 0;
        for(char key: HashMapMTL.keySet()) {
            Recordlist = HashMapMTL.get(key);
            for (int i = 0; i < Recordlist.size(); i++) {
                count++;
            }
        }
        return count;
    }

    private void load(){

        ObjectInputStream l_ois = null;
        try {

            l_ois = new ObjectInputStream(new FileInputStream(FilePath + "\\" + "LogFile" + "\\" + "MTLFile" + "\\" + "MTLServer" + ".txt"));
            HashMapMTL = (HashMap<Character, ArrayList<Record>>) l_ois.readObject();

        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("The Map is Empty!");
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            FileOutputStream l_saveFile = null;
            l_saveFile = new FileOutputStream(FilePath + "/" + "LogFile" + "/" + "MTLFile" + "/" + "MTLServer" + ".txt");
            ObjectOutputStream l_Save = new ObjectOutputStream(l_saveFile);
            l_Save.writeObject(HashMapMTL);
            l_Save.flush();
            l_Save.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("write object success!");
    }

    public  String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = date.toString();
        return time;
    }

    /**
     * Write the information to the logfile.
     * @param log the Operation details.
     */
    public void writeLog(String log){
        //Create, if no files.
        if(!this.loggingFile.exists()){
            try {
                this.loggingFile.createNewFile();
                FileWriter fileWriter = new FileWriter(this.loggingFile, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(log);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Open the file.
        else{
            try {
                synchronized (this.loggingFile) {
                    FileWriter fileWriter = new FileWriter(this.loggingFile, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(log);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean createTRecord(String managerID, String firstName, String lastName, String Address, String Phone, String Specialization, String Location) {

        if(!(Location.equals("mtl")||Location.equals("lvl")||Location.equals("ddo"))){
            System.out.println("The location is invalid.");
            return false;
        }
        TeacherRecord NewTRecord = new TeacherRecord(firstName, lastName, Address, Phone, Specialization, Location);

        ArrayList<Record> Recordlist = new ArrayList<>();


        if(managerID.startsWith("MTL")){

            MTLcount = getCount();
            NewTRecord.setRecordID(MTLcount);

            // Get the first letter.
            char Mark;
            Mark = lastName.charAt(0);

            //Put new record in the record list.
            if(HashMapMTL.containsKey(Mark)){
                Recordlist = HashMapMTL.get(Mark);
                Recordlist.add(NewTRecord);
                HashMapMTL.replace(Mark, Recordlist);

            }
            //Create new first letter key and the record list.
            else{
                Recordlist.add(NewTRecord);
                HashMapMTL.put(Mark, Recordlist);
            }

        }

        String writeInLog = "ManagerID: " + managerID + "\n" +
                "Create Teacher Record." + "\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "Address: " + Address + " " + "\n" +
                "Phone: " + Phone + " " + "\n" +
                "Specialization: " + Specialization + " " + "\n" +
                "Location: " + Location + " " + "\n" +
                "Time: " + getTime() + " " + "\n" + "\n";
        writeLog(writeInLog);
        save();
        return true;
    }


    /**
     * Determine whether student records can be created
     * @param managerID managerID
     * @param firstName firstName
     * @param lastName lastName
     * @param CoursesRegistered CoursesRegistered
     * @param Status Status
     * @param StatusDate StatusDate
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean createSRecord(String managerID, String firstName, String lastName, String CoursesRegistered, String Status, String StatusDate){

        if(!(Status.equals("active")||Status.equals("inactive"))){
            System.out.println("The Status is invalid.");
            return false;
        }
        StudentRecord NewSRecord = new StudentRecord(firstName, lastName, CoursesRegistered, Status, StatusDate);
        MTLcount = getCount();
        NewSRecord.setRecordID(MTLcount);
        ArrayList<Record> Recordlist = new ArrayList<>();

        if(managerID.startsWith("MTL")){
            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapMTL.containsKey(Mark)){
                Recordlist = HashMapMTL.get(Mark);
                Recordlist.add(NewSRecord);
                HashMapMTL.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewSRecord);
                HashMapMTL.put(Mark, Recordlist);
            }
        }

        else{
            System.out.println("Access Deny!(ManagerID is invalid)");
            return false;
        }

        String writeInLog = "ManagerID: " + managerID + "\n" +
                "Create Student Record." + "\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "CoursesRegister: " + CoursesRegistered + " " + "\n" +
                "Status: " + Status + " " + "\n" +
                "StatusDate: " + StatusDate + " " + "\n" +
                "Time: " + getTime() + " " + "\n" + "\n";
        this.writeLog(writeInLog);
        save();

        return true;

    }

    //Determine whether the teacher record can be edited or changed
    @Override
    public boolean editRecord(String managerID, String recordID, String fieldName, String newValue){

        Collection<ArrayList<Record>> allRecord = new ArrayList<>();
        int mark = 0;
        boolean result = false;
        Record target = null;
        if(managerID.startsWith("MTL")){

            allRecord = HashMapMTL.values();

            for(ArrayList<Record> recordList : allRecord){
                for(Record record : recordList){

                    if (record.getID().equals(recordID)){
                        target = record;
                        mark = 1;
                    }
                    break;
                }
                if(mark == 1){
                    break;
                }
            }
            if(target != null){
                if(target instanceof TeacherRecord){
                    synchronized (target) {
                        result = ((TeacherRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                else {
                    synchronized (target) {

                        result = ((StudentRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                String writeInLog = "Edit Record." + "\n" +
                        "ManagerID: " + managerID + "\n" +
                        "RecordID: " + target.getID() + "\n" +
                        "fieldName: " + fieldName + " " + "\n" +
                        "newValue: " + newValue + " " + "\n" +
                        "Time: " + getTime() + " " + "\n" + "\n";
                writeLog(writeInLog);
                save();
            }
            else{
                System.out.println("No Record.");
                result = false;
            }
        }

        return result;
    }

    //Print the record to the server in the corresponding region
    @Override
    public boolean printRecord(String managerID) {

        ArrayList<Record> Recordlist = new ArrayList<>();
        if(managerID.startsWith("MTL")){

            for(char key: HashMapMTL.keySet()) {

                System.out.print("\n" + key + ", ");
                Recordlist = HashMapMTL.get(key);
                for (int i = 0; i < Recordlist.size(); i++) {
                    System.out.print("{ID:" + Recordlist.get(i).getID() + " Name: " + Recordlist.get(i).getName() + "} ");
                }
                System.out.println("\n");
            }

        }
        return true;
    }

    @Override
    public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName) {
        return false;
    }

    //Get the number of records of all servers£¨include current server£© from the current server
    @Override
    public String getRecordCounts(){
        String sendStr = "MTL " + String.valueOf(MTLcount);
        return sendStr;
    }


    public void setServer(ORB orb) {
        this.orb = orb;
    }


}

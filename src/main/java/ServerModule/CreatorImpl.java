package ServerModule;

import recordFile.Record;
import recordFile.TeacherRecord;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreatorImpl extends CreatorPOA{

    File loggingFile = new File("");
    String FilePath = loggingFile.getAbsolutePath();

    HashMap<Character, ArrayList<Record>> HashMapMTL = new HashMap<Character, ArrayList<Record>>();
    HashMap<Character, ArrayList<Record>> HashMapLVL = new HashMap<Character, ArrayList<Record>>();
    HashMap<Character, ArrayList<Record>> HashMapDDO = new HashMap<Character, ArrayList<Record>>();
    int DDOcount = 0;
    int LVLcount = 0;
    int MTLcount = 0;

    public CreatorImpl(String ManagerID) {
        init();
    }

    //初始化
    private void init() {

        loggingFile = new File( FilePath + "\\" + "LogFile" + "\\" + "DDOFile"+ "\\" + "DDOLog" +".txt");
        if(!loggingFile.exists()){
            try {
                loggingFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //将用户表保存到本地文件中
    private void save() {
        try {
            FileOutputStream l_saveFile = null;
            l_saveFile = new FileOutputStream(FilePath + "/" + "LogFile" + "/" + "DDOFile" + "/" + "DDOServer" + ".txt");
            ObjectOutputStream l_Save = new ObjectOutputStream(l_saveFile);
            l_Save.writeObject(HashMapDDO);
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

            MTLcount++;
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
        else if(managerID.startsWith("LVL")){

            LVLcount++;
            NewTRecord.setRecordID(LVLcount);
            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapLVL.containsKey(Mark)){
                Recordlist = HashMapLVL.get(Mark);
                Recordlist.add(NewTRecord);
                HashMapLVL.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewTRecord);
                HashMapLVL.put(Mark, Recordlist);
            }
        }
        else if(managerID.startsWith("DDO")){

            DDOcount++;
            NewTRecord.setRecordID(DDOcount);
            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapDDO.containsKey(Mark)){
                Recordlist = HashMapDDO.get(Mark);
                Recordlist.add(NewTRecord);
                HashMapDDO.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewTRecord);
                HashMapDDO.put(Mark, Recordlist);
            }
        }
        else{
            System.out.println("Access Deny!(ManagerID is invalid)");
            return false;
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


    @Override
    public boolean createSRecord(String managerID, String firstName, String lastName, String CoursesRegistered, String Status, String StatusDate) {
        return false;
    }

    @Override
    public boolean editRecord(String managerID, String recordID, String fieldName, String newValue) {
        return false;
    }

    @Override
    public boolean printRecord(String ManagerID) {
        return false;
    }

    @Override
    public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName) {
        return false;
    }

    @Override
    public String getRecordCounts() {
        return null;
    }
}

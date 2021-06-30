package ServerModule;


/**
* ServerModule/CreatorOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��Server
* 2021��6��29�� ���ڶ� ����04ʱ52��12�� CST
*/

public interface CreatorOperations 
{
  boolean createTRecord (String managerID, String firstName, String lastName, String Address, String Phone, String Specialization, String Location);
  boolean createSRecord (String managerID, String firstName, String lastName, String CoursesRegistered, String Status, String StatusDate);
  boolean editRecord (String managerID, String recordID, String fieldName, String newValue);
  boolean printRecord (String ManagerID);
  boolean transferRecord (String managerID, String recordID, String remoteCenterServerName);
  String getRecordCounts ();
} // interface CreatorOperations

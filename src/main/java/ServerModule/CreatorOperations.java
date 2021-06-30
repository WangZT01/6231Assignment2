package ServerModule;


/**
* ServerModule/CreatorOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从Server
* 2021年6月29日 星期二 下午04时52分12秒 CST
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

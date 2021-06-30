package ServerModule;

/**
* ServerModule/CreatorHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从Server
* 2021年6月29日 星期二 下午04时52分12秒 CST
*/

public final class CreatorHolder implements org.omg.CORBA.portable.Streamable
{
  public ServerModule.Creator value = null;

  public CreatorHolder ()
  {
  }

  public CreatorHolder (ServerModule.Creator initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ServerModule.CreatorHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ServerModule.CreatorHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ServerModule.CreatorHelper.type ();
  }

}

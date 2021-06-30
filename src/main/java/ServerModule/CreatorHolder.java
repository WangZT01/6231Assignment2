package ServerModule;

/**
* ServerModule/CreatorHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��Server
* 2021��6��29�� ���ڶ� ����04ʱ52��12�� CST
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

package ServerModule;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

public class MTLServer {



    public static void main(String[] args) {

        ORB orb;
        POA rootPOA;
        org.omg.CORBA.Object obj;
        CreatorImplMTL creatorImplMTL;
        org.omg.CORBA.Object ref;
        Creator creatorhref;
        org.omg.CORBA.Object objRef;
        NamingContextExt ncRef;

        try {

            Properties properties = new Properties();

            properties.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");  //ָ��ORB��ip��ַ
            properties.put("org.omg.CORBA.ORBInitialPort", "6001");       //ָ��ORB�Ķ˿�
            args = new String[2];
            args[0] = "-ORBInitialPort";
            args[1] = "1050";

            //����һ��ORBʵ��
            orb = ORB.init(args,null);

            //�õ���POA������,������POAManager,�൱��������server
            obj = orb.resolve_initial_references("RootPOA");
            rootPOA = POAHelper.narrow(obj);
            rootPOA.the_POAManager().activate();

            //����һ��CreatorImplʵ��
            creatorImplMTL = new CreatorImplMTL();
            creatorImplMTL.setServer(orb);

            //�ӷ����еõ����������,��ע�ᵽ������
            ref = rootPOA.servant_to_reference(creatorImplMTL);
            creatorhref = CreatorHelper.narrow(ref);

            //�õ�һ����������������
            objRef = orb.resolve_initial_references("NameService");
            ncRef = NamingContextExtHelper.narrow(objRef);

            //�������������а��������
            String name = "Creator";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, creatorhref);

            System.out.println("server.ToDoListServer is ready and waiting....");

            //�����̷߳���,�ȴ��ͻ��˵���
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

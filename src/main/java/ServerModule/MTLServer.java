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

            properties.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");  //指定ORB的ip地址
            properties.put("org.omg.CORBA.ORBInitialPort", "6001");       //指定ORB的端口
            args = new String[2];
            args[0] = "-ORBInitialPort";
            args[1] = "1050";

            //创建一个ORB实例
            orb = ORB.init(args,null);

            //拿到根POA的引用,并激活POAManager,相当于启动了server
            obj = orb.resolve_initial_references("RootPOA");
            rootPOA = POAHelper.narrow(obj);
            rootPOA.the_POAManager().activate();

            //创建一个CreatorImpl实例
            creatorImplMTL = new CreatorImplMTL();
            creatorImplMTL.setServer(orb);

            //从服务中得到对象的引用,并注册到服务中
            ref = rootPOA.servant_to_reference(creatorImplMTL);
            creatorhref = CreatorHelper.narrow(ref);

            //得到一个根命名的上下文
            objRef = orb.resolve_initial_references("NameService");
            ncRef = NamingContextExtHelper.narrow(objRef);

            //在命名上下文中绑定这个对象
            String name = "Creator";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, creatorhref);

            System.out.println("server.ToDoListServer is ready and waiting....");

            //启动线程服务,等待客户端调用
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.lubsh.zookeeper.base;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * Zookeeper base学习笔记
 * @since 2019-3-5
 */
public class ZookeeperBase {

    /** zookeeper地址 */
    static final String CONNECT_ADDR = "192.168.90.101:2181,192.168.90.102:2181,192.168.90.103:2181";
    /** session超时时间 */
    static final int SESSION_OUTTIME = 5000;//ms
    /** 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号 */
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{

        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                //获取事件的状态
                KeeperState keeperState = event.getState();
                //获取时间的类型
                EventType eventType = event.getType();
                //如果是建立连接x#
                if(KeeperState.SyncConnected == keeperState){
                    if(EventType.None == eventType){
                        //如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        connectedSemaphore.countDown();
                        System.out.println("zk 建立连接");
                    }
                }
            }
        });

        //进行阻塞
        connectedSemaphore.await();
        System.out.println("执行啦..");

        //创建父节点
//		String ret = zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);  //PERSISTENT 永久节点
//        System.out.println(ret);

        //创建子节点
//        String ret = zk.create("/testRoot/children", "children data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT); //EPHEMERAL 临时节点
//        System.out.println(ret);

        //获取节点洗信息
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));

        //获取多个子节点信息（只支持直接字节点）
        /*List<String> list = zk.getChildren("/testRoot",false,null);
        for (String path : list){
            String realPath = "/testRoot/" + path;
            System.out.println(realPath);
            byte[] data = zk.getData(realPath, false, null);
            System.out.println(new String(data));
        }*/

        //修改节点的值
//		zk.setData("/testRoot", "modify data root".getBytes(), -1);
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));

        //判断节点是否存在
//		System.out.println(zk.exists("/testRoot/children", false));

        //删除节点
//		zk.delete("/testRoot/children", -1);
//		System.out.println(zk.exists("/testRoot/children", false));

        //异步删除节点（有回调）
        /*zk.delete("/testRoot", -1,
                new AsyncCallback.VoidCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx) {
                        System.out.println(rc);
                        System.out.println(path);
                        System.out.println(ctx);
                    }
                },
                "a");*/

        zk.close();
    }
}
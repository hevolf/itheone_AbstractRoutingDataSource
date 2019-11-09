package cn.enjoyedu.rwseparation.dbutils;

import cn.enjoyedu.rwseparation.vo.DBTypeEnum;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：数据源的持有
 */
public class DBContextHolder {
    /*保存系统中存在的数据源的标识符，然后通过该标识符定位到实际的数据源实体*/
    private static final ThreadLocal<DBTypeEnum> contextHolder
            = new ThreadLocal<DBTypeEnum>();

    public static void set(DBTypeEnum dbTypeEnum){
        contextHolder.set(dbTypeEnum);
    }

    public static DBTypeEnum get(){
        return contextHolder.get();
    }

    public static void master(){
        set(DBTypeEnum.MASTER);
        System.out.println("切换到主库-----------------------");
    }

    public static void slave(){
        set(DBTypeEnum.SLAVE);//轮询
        System.out.println("切换到从库-----------------------");
    }

}

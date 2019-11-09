package cn.enjoyedu.rwseparation.service;

import cn.enjoyedu.rwseparation.annotation.Master;
import cn.enjoyedu.rwseparation.dao.OrderExpMapper;
import cn.enjoyedu.rwseparation.model.OrderExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com 
 *
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2133576719
 *
 *类说明：订单相关的服务
 */
@Service
public class ProcessOrder {
	
	private Logger logger = LoggerFactory.getLogger(ProcessOrder.class);
	
	public final static short UNPAY = 0;//未支付
	public final static short PAYED = 1;//已支付
	public final static short EXPIRED = -1;//已过期
	
	@Autowired
	private OrderExpMapper orderExpMapper;
	
	/**
	 * 接收前端页面参数，生成订单
	 * @param orderNumber 订单个数
	 */
	@Transactional
    public void insertOrders(int orderNumber){
    	Random r = new Random();
    	OrderExp orderExp ;
    	for(int i=0;i<orderNumber;i++) {
    		long expireTime = r.nextInt(20)+5;//订单的超时时长，单位秒5~25
            orderExp = new OrderExp();
            String orderNo = "DD00_"+expireTime+"S";//订单的编号
            orderExp.setOrderNo(orderNo);
            orderExp.setOrderNote("海王5排"+expireTime+"号,过期时长:"+orderNo);
            orderExp.setOrderStatus(UNPAY);//订单的状态，目前为未支付
            orderExpMapper.insertDelayOrder(orderExp,expireTime);
            logger.info("保存订单到DB:"+orderNo);

    	}
    }

	public void findOrders(){
		List<OrderExp> orderList= orderExpMapper.selectUnPayOrders();
		logger.info("发现了表中还有["+orderList.size()
				+"]个未到期未支付的订单！");
		for(OrderExp order:orderList)
		{
			logger.info(order.toString());
		}
	}

	@Master
	public void findOrdersMaster(){
		List<OrderExp> orderList= orderExpMapper.selectUnPayOrders();
		logger.info("发现了表中还有["+orderList.size()
				+"]个未到期未支付的订单！");
		for(OrderExp order:orderList)
		{
			logger.info(order.toString());
		}
	}


}

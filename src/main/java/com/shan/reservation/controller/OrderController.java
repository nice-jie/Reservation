package com.shan.reservation.controller;

import com.shan.reservation.bean.food;
import com.shan.reservation.bean.order;
import com.shan.reservation.bean.user;
import com.shan.reservation.mapper.userMapper;
import com.shan.reservation.service.FoodService;
import com.shan.reservation.service.OrderService;
import com.shan.reservation.util.ArchivesLog;
import com.shan.reservation.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wsw
 * @Package com.gabe.mychat.controller
 * @Description:订单controller
 * @date 2020年1月14日 18:38:51
 */
@Controller
public class OrderController {
    @Autowired
    OrderService OrderService;
    @Autowired
    userMapper userMapper;
    @ResponseBody
    @RequestMapping("/selectOrderByUserId" )
    @ArchivesLog(operationType = "查询信息", operationName = "查询用户订单信息")
    public R selectOrderByUserId(@RequestBody Map<String,String> map, HttpSession httpSession){
        int userId=Integer.parseInt(map.get("userId"));
        List<order> list=OrderService.selectByUserId(userId);
        return  R.ok().put("order",list);
    }
    @ResponseBody
    @RequestMapping("/deleteOrderByNo" )
    @ArchivesLog(operationType = "删除信息", operationName = "删除订单")
    public R deleteOrderByNo(@RequestBody Map<String,String> map, HttpSession httpSession){
        String orderNo=map.get("orderno");
        OrderService.deleteByNo(orderNo);
        return  R.ok();
    }
    @ResponseBody
    @RequestMapping("/deleteOrderByNo2" )
    @ArchivesLog(operationType = "删除信息", operationName = "删除订单")
    public R deleteOrderByNo2(@RequestBody Map<String,String> map, HttpSession httpSession){
        String orderNo=map.get("orderno");
        OrderService.deleteByNo2(orderNo);
        return  R.ok();
    }
    @ResponseBody
    @RequestMapping("/AddOrder" )
    @ArchivesLog(operationType = "添加信息", operationName = "创建订单")
    public R AddOrder(@RequestBody Map<String,String> map, HttpSession httpSession){
        //获取订单基本信息
        String orderNo_user=map.get("userId");
        Integer user_id=Integer.parseInt(map.get("userId"));
        user user= userMapper.selectByPrimaryKey(user_id);
        Integer address_id=Integer.parseInt(map.get("addressId"));
        String user_name=user.getUserName();
        Integer res_id=9;
        String price2= map.get("price");
        BigDecimal price=new BigDecimal(price2);
        Date date=new Date();
        Byte state=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderNo_date = sdf.format(date);
        String orderNo=orderNo_date+orderNo_user;
        order order=new order(orderNo,user_id,res_id,price,date,state,address_id);
        //创建订单
        OrderService.addOrder(order);
        return R.ok().put("order",order);
    }
    @ResponseBody
    @RequestMapping("/selectOrder" )
    @ArchivesLog(operationType = "查询信息", operationName = "根据编号查询订单")
    public R selectOrder(@RequestBody Map<String,String> map, HttpSession httpSession){
        String orderNo=map.get("orderNo");
        order order=OrderService.selectOrder(orderNo);
        return R.ok().put("order",order);
    }
    @ResponseBody
    @RequestMapping("/ResSelectOrder" )
    @ArchivesLog(operationType = "查询信息", operationName = "商家查询订单")
    public R ResSelectOrder(@RequestBody Map<String,String> map, HttpSession httpSession){
        Integer restaurantid=Integer.parseInt(map.get("restaurantid"));
        List<order> order=OrderService.ResSelectOrder(restaurantid);
        List tarlist=new ArrayList();
        Iterator it=order.iterator();
        while(it.hasNext()){
            Map tarmap=new HashMap();
            order order1=(order)it.next();
            String orderno=order1.getOrderno();
            BigDecimal price=order1.getPrice();
            int user=order1.getUserid();
            user user1=userMapper.selectByPrimaryKey(user);
            String nickName=user1.getUserName();
            String userName=user1.getRealname();
            String userPhone=user1.getUserPhone();
            Date createtime=order1.getCreatetime();
            Byte orderstate=order1.getOrderstate();
            tarmap.put("orderno",orderno);
            tarmap.put("price",price);
            tarmap.put("userName",userName);
            tarmap.put("nickName",nickName);
            tarmap.put("userPhone",userPhone);
            tarmap.put("createtime",createtime);
            tarmap.put("orderstate",orderstate);
            tarlist.add(tarmap);
        }
        return R.ok().put("order",tarlist);
    }
}

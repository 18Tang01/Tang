
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 课程预约
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/kechengOrder")
public class KechengOrderController {
    private static final Logger logger = LoggerFactory.getLogger(KechengOrderController.class);

    @Autowired
    private KechengOrderService kechengOrderService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service
    @Autowired
    private HuiyuanService huiyuanService;
    @Autowired
    private KechengService kechengService;



    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("会员".equals(role))
            params.put("huiyuanId",request.getSession().getAttribute("userId"));
        else if("员工".equals(role))
            params.put("yuangongId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = kechengOrderService.queryPage(params);

        //字典表数据转换
        List<KechengOrderView> list =(List<KechengOrderView>)page.getList();
        for(KechengOrderView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        KechengOrderEntity kechengOrder = kechengOrderService.selectById(id);
        if(kechengOrder !=null){
            //entity转view
            KechengOrderView view = new KechengOrderView();
            BeanUtils.copyProperties( kechengOrder , view );//把实体数据重构到view中

                //级联表
                HuiyuanEntity huiyuan = huiyuanService.selectById(kechengOrder.getHuiyuanId());
                if(huiyuan != null){
                    BeanUtils.copyProperties( huiyuan , view ,new String[]{ "id", "createTime", "insertTime", "updateTime"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setHuiyuanId(huiyuan.getId());
                }
                //级联表
                KechengEntity kecheng = kechengService.selectById(kechengOrder.getKechengId());
                if(kecheng != null){
                    BeanUtils.copyProperties( kecheng , view ,new String[]{ "id", "createTime", "insertTime", "updateTime"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setKechengId(kecheng.getId());
                }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody KechengOrderEntity kechengOrder, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,kechengOrder:{}",this.getClass().getName(),kechengOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("会员".equals(role))
            kechengOrder.setHuiyuanId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        kechengOrder.setInsertTime(new Date());
        kechengOrder.setCreateTime(new Date());
        kechengOrderService.insert(kechengOrder);
        return R.ok();
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody KechengOrderEntity kechengOrder, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,kechengOrder:{}",this.getClass().getName(),kechengOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("会员".equals(role))
//            kechengOrder.setHuiyuanId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        //根据字段查询是否有相同数据
        Wrapper<KechengOrderEntity> queryWrapper = new EntityWrapper<KechengOrderEntity>()
            .eq("id",0)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        KechengOrderEntity kechengOrderEntity = kechengOrderService.selectOne(queryWrapper);
        if(kechengOrderEntity==null){
            kechengOrderService.updateById(kechengOrder);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        kechengOrderService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<KechengOrderEntity> kechengOrderList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("../../upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            KechengOrderEntity kechengOrderEntity = new KechengOrderEntity();
//                            kechengOrderEntity.setKechengOrderUuidNumber(data.get(0));                    //预约流水号 要改的
//                            kechengOrderEntity.setKechengId(Integer.valueOf(data.get(0)));   //课程 要改的
//                            kechengOrderEntity.setHuiyuanId(Integer.valueOf(data.get(0)));   //会员 要改的
//                            kechengOrderEntity.setKechengOrderTruePrice(data.get(0));                    //实付金额 要改的
//                            kechengOrderEntity.setKechengOrderTypes(Integer.valueOf(data.get(0)));   //课程预约状态 要改的
//                            kechengOrderEntity.setInsertTime(date);//时间
//                            kechengOrderEntity.setCreateTime(date);//时间
                            kechengOrderList.add(kechengOrderEntity);


                            //把要查询是否重复的字段放入map中
                                //预约流水号
                                if(seachFields.containsKey("kechengOrderUuidNumber")){
                                    List<String> kechengOrderUuidNumber = seachFields.get("kechengOrderUuidNumber");
                                    kechengOrderUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> kechengOrderUuidNumber = new ArrayList<>();
                                    kechengOrderUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("kechengOrderUuidNumber",kechengOrderUuidNumber);
                                }
                        }

                        //查询是否重复
                         //预约流水号
                        List<KechengOrderEntity> kechengOrderEntities_kechengOrderUuidNumber = kechengOrderService.selectList(new EntityWrapper<KechengOrderEntity>().in("kecheng_order_uuid_number", seachFields.get("kechengOrderUuidNumber")));
                        if(kechengOrderEntities_kechengOrderUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(KechengOrderEntity s:kechengOrderEntities_kechengOrderUuidNumber){
                                repeatFields.add(s.getKechengOrderUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [预约流水号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        kechengOrderService.insertBatch(kechengOrderList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }





    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        // 没有指定排序字段就默认id倒序
        if(StringUtil.isEmpty(String.valueOf(params.get("orderBy")))){
            params.put("orderBy","id");
        }
        PageUtils page = kechengOrderService.queryPage(params);

        //字典表数据转换
        List<KechengOrderView> list =(List<KechengOrderView>)page.getList();
        for(KechengOrderView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段
        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        KechengOrderEntity kechengOrder = kechengOrderService.selectById(id);
            if(kechengOrder !=null){


                //entity转view
                KechengOrderView view = new KechengOrderView();
                BeanUtils.copyProperties( kechengOrder , view );//把实体数据重构到view中

                //级联表
                    HuiyuanEntity huiyuan = huiyuanService.selectById(kechengOrder.getHuiyuanId());
                if(huiyuan != null){
                    BeanUtils.copyProperties( huiyuan , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setHuiyuanId(huiyuan.getId());
                }
                //级联表
                    KechengEntity kecheng = kechengService.selectById(kechengOrder.getKechengId());
                if(kecheng != null){
                    BeanUtils.copyProperties( kecheng , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setKechengId(kecheng.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody KechengOrderEntity kechengOrder, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,kechengOrder:{}",this.getClass().getName(),kechengOrder.toString());
            KechengEntity kechengEntity = kechengService.selectById(kechengOrder.getKechengId());
            if(kechengEntity == null){
                return R.error(511,"查不到该课程");
            }
            // Double kechengNewMoney = kechengEntity.getKechengNewMoney();

            if(false){
            }
            else if(kechengEntity.getKechengNewMoney() == null){
                return R.error(511,"课程价格不能为空");
            }


        KechengOrderEntity kechengOrderEntity = kechengOrderService.selectOne(
                new EntityWrapper<KechengOrderEntity>()
                        .eq("kecheng_id", kechengOrder.getKechengId())
                        .eq("huiyuan_id", kechengOrder.getHuiyuanId())
                        .notIn("kecheng_order_types", 2)
        );
        if(kechengOrderEntity != null)
            return R.error("该课程该用户已经预约过了,不能重复预约");


        List<KechengOrderEntity> kechengOrderEntities = kechengOrderService.selectList(
                new EntityWrapper<KechengOrderEntity>()
                        .eq("kecheng_id", kechengOrder.getKechengId())
                        .notIn("kecheng_order_types", 2)
        );

        if(kechengEntity.getKechengNumber()<=kechengOrderEntities.size())
            return R.error("该课程已经预约满了,无法再预约了,赶紧看看其他课程吧");


        //计算所获得积分
            Double buyJifen =0.0;
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            HuiyuanEntity huiyuanEntity = huiyuanService.selectById(userId);
            if(huiyuanEntity == null)
                return R.error(511,"用户不能为空");
            if(huiyuanEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");
            double balance = huiyuanEntity.getNewMoney() - kechengEntity.getKechengNewMoney();//余额
            if(balance<0)
                return R.error(511,"预约不成功,当前账户不足支付此次课程预约");
            kechengOrder.setKechengOrderTypes(1); //设置订单状态为已支付
            kechengOrder.setKechengOrderTruePrice(kechengEntity.getKechengNewMoney()); //设置实付价格
            kechengOrder.setHuiyuanId(userId); //设置订单支付人id
            kechengOrder.setKechengOrderUuidNumber(String.valueOf(new Date().getTime()));
            kechengOrder.setInsertTime(new Date());
            kechengOrder.setCreateTime(new Date());
                kechengOrderService.insert(kechengOrder);//新增订单
            huiyuanEntity.setNewMoney(balance);//设置金额
            huiyuanService.updateById(huiyuanEntity);
            return R.ok();
    }

    /**
    * 退款
    */
    @RequestMapping("/refund")
    public R refund(Integer id, HttpServletRequest request){
        logger.debug("refund方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        String role = String.valueOf(request.getSession().getAttribute("role"));

            KechengOrderEntity kechengOrder = kechengOrderService.selectById(id);
            Integer kechengId = kechengOrder.getKechengId();
            if(kechengId == null)
                return R.error(511,"查不到该课程");
            KechengEntity kechengEntity = kechengService.selectById(kechengId);
            if(kechengEntity == null)
                return R.error(511,"查不到该课程");
            Double kechengNewMoney = kechengEntity.getKechengNewMoney();
            if(kechengNewMoney == null)
                return R.error(511,"课程价格不能为空");

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            HuiyuanEntity huiyuanEntity = huiyuanService.selectById(userId);
            if(huiyuanEntity == null)
                return R.error(511,"用户不能为空");
            if(huiyuanEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");

            Double zhekou = 1.0;


            //判断是什么支付方式 1代表余额 2代表积分
                //计算金额
                Double money = kechengEntity.getKechengNewMoney() * 1  * zhekou;
                //计算所获得积分
                Double buyJifen = 0.0;
                huiyuanEntity.setNewMoney(huiyuanEntity.getNewMoney() + money); //设置金额






            kechengOrder.setKechengOrderTypes(2);//设置订单状态为退款
            kechengOrderService.updateById(kechengOrder);//根据id更新
            huiyuanService.updateById(huiyuanEntity);//更新用户信息
            kechengService.updateById(kechengEntity);//更新订单中课程的信息
            return R.ok();
    }


    /**
     * 确认使用
     */
    @RequestMapping("/deliver")
    public R deliver(Integer id ){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        KechengOrderEntity  kechengOrderEntity = new  KechengOrderEntity();;
        kechengOrderEntity.setId(id);
        kechengOrderEntity.setKechengOrderTypes(3);
        boolean b =  kechengOrderService.updateById( kechengOrderEntity);
        if(!b){
            return R.error("确认使用出错");
        }
        return R.ok();
    }

















}

package com.xxxx.localism.service.impl;

import com.xxxx.localism.pojo.Passage;
import com.xxxx.localism.mapper.PassageMapper;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.service.IPassageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-05-20
 */
@Service
public class PassageServiceImpl extends ServiceImpl<PassageMapper, Passage> implements IPassageService {

    @Resource
    private PassageMapper passageMapper;

//    @Override
//    public RespPageBean getAllPassageByMenuId(Integer id, Integer currentPage, Integer size) {
//        Page<Passage> passagePage=passageMapper.getAllPassageByMenuId(id,currentPage,size);
////        List<Passage> list=passageMapper.getAllPassageByMenuId(id,currentPage,size);
//        return new RespPageBean(passagePage.getTotal(),passagePage.getRecords());
//    }
    @Override
    public RespBean getAllPassageByMenuId(Integer id, Integer currentPage, Integer size, String how) {
        List<Passage> list=passageMapper.getAllPassageByMenuId(id,(currentPage-1)*size,size,how);
        if(list.size()!=0){
            return RespBean.success("获取成功",list);
        }
        return RespBean.error("获取失败");
    }

    @Override
    public RespBean getSinglePassageInfo(Integer id,Integer adminId) {
        Passage passage= new Passage();
        passage=passageMapper.getSinglePassageInfo(id);
        System.out.println(passage);
        if(passage.getDeleted()==1){
            return RespBean.error("文章不存在");
        }
        Boolean isStar=passageMapper.getStar(id,adminId);
        Boolean isCollect=passageMapper.getCollect(id,adminId);
        if(isStar)
            passage.setStar(isStar);
        if(isCollect)
            passage.setCollect(isCollect);
        if(passage.getId()!=null){
            return RespBean.success("获取成功!",passage);
        }
        return RespBean.error("获取失败");
    }
}

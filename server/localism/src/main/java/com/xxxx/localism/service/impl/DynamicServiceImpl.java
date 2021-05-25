package com.xxxx.localism.service.impl;

import com.xxxx.localism.pojo.Dynamic;
import com.xxxx.localism.mapper.DynamicMapper;
import com.xxxx.localism.pojo.RespBean;
import com.xxxx.localism.service.IDynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.localism.utils.FastDFSUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-05-24
 */
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements IDynamicService {

    @Resource
    private DynamicMapper dynamicMapper;
    /**
     * 上传图片
     * @param file
     * @return
     */
    @Override
    public RespBean uploadImage(MultipartFile file) {
        String[] filePath = FastDFSUtils.upload(file);
        if(filePath.length==2){
            return RespBean.success("上传成功",FastDFSUtils.getTrackerUrl()+filePath[0]+"/"+filePath[1]);
        }
        return RespBean.error("上传失败");
    }

    /**
     * 发布动态
     * @param content
     * @param adminId
     * @param tags
     * @param url
     * @return
     */
    @Override
    public RespBean addDynamic(String content, Integer adminId, String tags, String[] url) {
        String image="";
        for(int i=0;i<url.length;i++)
        {
            image=image+url[i]+",";
        }
        Dynamic dynamic=new Dynamic();
        dynamic.setAdminId(adminId);
        dynamic.setContent(content);
        dynamic.setImage(image.substring(0,image.length()-1));
        dynamic.setCreateTime(new Date());
        dynamic.setTags(tags);
        int result = dynamicMapper.insert(dynamic);
        if(result==1){
            return RespBean.success("发布成功");
        }
        return RespBean.error("发布失败");
    }

    /**
     * 获取所有动态信息
     * @param chooseWhat
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public RespBean getAllDynamicInfo(Integer chooseWhat, Integer currentPage, Integer size) {
        String str="";
        String t2="";
        if(chooseWhat==0){//点赞量
            str="d.stars";
        }else if(chooseWhat==1){ //创建时间
            str="d.create_time";
        }
        List<Dynamic> lists = dynamicMapper.getAllDynamicInfo((currentPage - 1) * size, size, str);
        for (Dynamic dynamic : lists) {
            dynamic.setUrl(dynamic.getImage().split(","));
        }
        if(lists.size()!=0){
            return RespBean.success("获取成功",lists);
        }
        return RespBean.error("获取失败");
    }
}

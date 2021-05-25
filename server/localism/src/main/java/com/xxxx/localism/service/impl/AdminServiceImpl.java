package com.xxxx.localism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.localism.mapper.AdminMapper;
import com.xxxx.localism.mapper.PassageMapper;
import com.xxxx.localism.mapper.VideoMapper;
import com.xxxx.localism.pojo.*;
import com.xxxx.localism.pojo.Error;
import com.xxxx.localism.service.IAdminService;
import com.xxxx.localism.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private PassageMapper passageMapper;
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private CommentServiceImpl commentService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Override
    public RespBean login(String username, String password,String code, HttpServletRequest request) {
        String captcha= (String) request.getSession().getAttribute("captcha");
        //比较验证码
        System.out.println("用户输入的验证码为:"+code);
//        if(StringUtils.isEmpty(code) ){
//            return RespBean.error("验证码未填写!");
//        }
//        if(!code.equals(captcha)){
//            return RespBean.error("验证码不正确!");
//        }
        //登录
        Admin userDetails=(Admin) userDetailsService.loadUserByUsername(username);
        //比较用户名和密码
        if(null==userDetails){
            return RespBean.error("不存在该用户名!");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("密码不正确!");
        }
        //查看账号是否被禁用
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁用,请联系管理员!");
        }
        //设置用户对象
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,
                null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //返回token
        String token=jwtTokenUtil.generateToken(userDetails);
        Map<String,String>tokenMap=new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登录成功!",tokenMap);
    }

    @Override
    public Admin getAdminByUserName(String username) {
//        return adminMapper.getAdminByUserName(username);
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username));
    }

    /**
     * 注册,保证,用户名,密码不能为空
     * @param adminLoginParam
     * @return
     */
    @Override
    public RespBean registerAdmin(AdminLoginParam adminLoginParam) {
        Admin adminTemp=adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",adminLoginParam.getUsername()));
        if(null!=adminTemp) {
            return RespBean.error("用户名已注册,请更换!");
        }
        List<Map<String, Object>> maps = adminMapper.selectMaps(new QueryWrapper<Admin>().select("max(id)"));
        Integer id=Integer.parseInt(maps.get(0).get("max(id)").toString())+1;
        Admin admin=new Admin();
        admin.setId(id);
        admin.setName("安小希");
        admin.setPassword(passwordEncoder.encode(adminLoginParam.getPassword()));
        admin.setUsername(adminLoginParam.getUsername());
        admin.setEnabled(true);
        if(1==adminMapper.insert(admin)){
            return RespBean.success("注册成功!");
        }
        return RespBean.error("注册失败!,请重试");

    }

    /**
     * 获取当前最大id
     * @return
     */
    @Override
    public RespBean maxId() {
        return null;
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param password
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String password, Integer adminId) {

        Admin admin = adminMapper.selectById(adminId);
        if(!passwordEncoder.matches(oldPass,admin.getPassword())){
            return RespBean.error("原密码输入错误!");
        }
        admin.setPassword(passwordEncoder.encode(password));
        if(1==adminMapper.updateById(admin)){
            return RespBean.success("修改密码成功!");
        }
        return RespBean.error("修改密码失败!");
    }

    /**
     * 获取所有用户(分页)
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public RespPageBean getAllAdminInfo(Integer currentPage, Integer size) {
        Page<Admin> page =new Page<>(currentPage,size);
        IPage<Admin> adminIPage=adminMapper.selectPage(page,null);
        RespPageBean respPageBean=new RespPageBean(adminIPage.getTotal(),adminIPage.getRecords());
        return respPageBean;
    }

    /**
     * 获取用户所在party
     * @param token
     * @return
     */
    @Override
    public Object getPartyId(String token) {
        if(!jwtTokenUtil.canRefresh(token)){
            return Error.error("token无效");
        }
        String username = jwtTokenUtil.getUserNameFromToken(token);
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
        if(admin!=null){
            return Success.success(admin.getId());
        }
        return Error.error("获取失败");
    }

    /**
     * 获取用户所在房间
     * @param user_id
     * @return
     */
    @Override
    public Object getAdminRoom(Integer user_id) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", user_id));
        Integer partyId = admin.getPartyId();
        if(partyId!=0)
            return Success.success(partyId);
        return Error.error("该用户当前不处在任何房间");
    }

    /**
     * 获取信息通过id
     * @param id
     */
    @Override
    public RespBean getAdminInfoById(Integer id) {
        Admin admin = null;
        admin=adminMapper.selectById(id);
        admin.setPassword("");
        if(admin!=null){
            return RespBean.success("获取成功",admin);
        }
        return RespBean.error("获取失败");
    }

    /**
     * 点赞
     * @param Id
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    @Override
    public RespBean addStar(Integer Id, Integer adminId, String table1, String table2, String id) {
        Integer result=adminMapper.addStar(Id,adminId,table1,table2,id);
        if(result==1){
            return RespBean.success("点赞成功!");
        }
        return RespBean.error("点赞失败");
    }

    /**
     * 取消点赞
     * @param videoId
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    @Override
    public RespBean deleteStar(Integer videoId, Integer adminId, String table1, String table2, String id) {
        Integer result=adminMapper.deleteStar(videoId,adminId,table1,table2,id);
        if(result==1){
            return RespBean.success("取消成功");
        }
        return RespBean.error("取消失败");
    }

    /**
     * 收藏
     * @param videoId
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    @Override
    @Transactional
    public RespBean addCollect(Integer videoId, Integer adminId, String table1, String table2, String id) {
        Integer result=adminMapper.addCollect(videoId,adminId,table1,table2,id);
        if(result==1){
            return RespBean.success("收藏成功!");
        }
        return RespBean.error("收藏失败");
    }

    /**
     * 取消收藏
     * @param videoId
     * @param adminId
     * @param table1
     * @param table2
     * @param id
     * @return
     */
    @Override
    public RespBean deleteCollect(Integer videoId, Integer adminId, String table1, String table2, String id) {
        Integer result=adminMapper.deleteCollect(videoId,adminId,table1,table2,id);
        if(result==1){
            return RespBean.success("取消成功");
        }
        return RespBean.error("取消失败");


    }

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    @Override
    public RespBean uploadUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if(1==result){
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,
                    null,authentication.getAuthorities()));
            return RespBean.success("更新成功!",url);
        }
        return RespBean.error("更新失败");
    }

    /**
     * 上传文件
     * @param url
     * @param adminId
     * @param chooseWhat
     * @param menuId
     * @param imageUrl
     * @param name
     * @return
     */
    @Override
    public RespBean uploadFile(String url, Integer adminId, Integer chooseWhat, Integer menuId, String imageUrl, String name) {
        Admin admin = adminMapper.selectById(adminId);
        if(!admin.getIsExpert()){
            return RespBean.error("您的权限不足，不能发布文章");
        }
        String t1="";
        String t2="";
        Date time =new Date();
        if(chooseWhat==1){
            t1="t_passage";
            t2="menu_passage";
        }else if(chooseWhat==0){
            t1="t_video";
            t2="menu_video";
        }
        int result = passageMapper.uploadFile(url,adminId,menuId,t1,t2,time,imageUrl,name);
        Comment comment=new Comment();
        comment.setAdminId(adminId);
        comment.setCreateTime(new Date());
        comment.setContent("");
        int id=0;
        if(chooseWhat==1){
            Passage passage = passageMapper.selectOne(new QueryWrapper<Passage>().eq("content", url));
            id=passage.getId();
            System.out.println("-------------------"+id);
        }else if(chooseWhat==0){
            Video video = videoMapper.selectOne(new QueryWrapper<Video>().eq("content", url));
            id=video.getId();
        }
        RespBean respBean = commentService.addComment(id, 0, comment, chooseWhat);
        Map<String,String> map=new HashMap<>();
        map.put("fileUrl",url);
        map.put("imageUrl",imageUrl);
        if(result==1){
            return RespBean.success("上传成功",map);
        }
        return RespBean.error("上传失败");
    }

    /**
     * 删除文件
     * @param adminId
     * @param id
     * @param chooseWhat
     * @return
     */
    @Override
    public RespBean deleteFile(Integer adminId, Integer id, Integer chooseWhat) {
        Admin admin = adminMapper.selectById(adminId);
        if(!admin.getIsExpert()){
            return RespBean.error("您的权限不足，无法删除");
        }
        int result=0;
        if(chooseWhat==0){
            Video video = videoMapper.selectById(id);
            if(video.getAdminId()!=adminId){
                return RespBean.error("这不是您发布的视频，无法删除");
            }
            result = videoMapper.deleteById(id);
        }else if(chooseWhat==1){
            Passage passage = passageMapper.selectById(id);
            if(passage.getAdminId()!=adminId){
                return RespBean.error("这不是您发布的文章，无法删除");
            }
            result=passageMapper.deleteById(id);
        }
        if(result==1){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }




}

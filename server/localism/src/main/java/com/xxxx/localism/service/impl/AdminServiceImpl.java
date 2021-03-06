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
        //???????????????
        System.out.println("???????????????????????????:"+code);
//        if(StringUtils.isEmpty(code) ){
//            return RespBean.error("??????????????????!");
//        }
//        if(!code.equals(captcha)){
//            return RespBean.error("??????????????????!");
//        }
        //??????
        Admin userDetails=(Admin) userDetailsService.loadUserByUsername(username);
        //????????????????????????
        if(null==userDetails){
            return RespBean.error("?????????????????????!");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("???????????????!");
        }
        //???????????????????????????
        if(!userDetails.isEnabled()){
            return RespBean.error("???????????????,??????????????????!");
        }
        //??????????????????
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,
                null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //??????token
        String token=jwtTokenUtil.generateToken(userDetails);
        Map<String,String>tokenMap=new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("????????????!",tokenMap);
    }

    @Override
    public Admin getAdminByUserName(String username) {
//        return adminMapper.getAdminByUserName(username);
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username));
    }

    /**
     * ??????,??????,?????????,??????????????????
     * @param adminLoginParam
     * @return
     */
    @Override
    public RespBean registerAdmin(AdminLoginParam adminLoginParam) {
        Admin adminTemp=adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",adminLoginParam.getUsername()));
        if(null!=adminTemp) {
            return RespBean.error("??????????????????,?????????!");
        }
        List<Map<String, Object>> maps = adminMapper.selectMaps(new QueryWrapper<Admin>().select("max(id)"));
        Integer id=Integer.parseInt(maps.get(0).get("max(id)").toString())+1;
        Admin admin=new Admin();
        admin.setId(id);
        admin.setName("?????????");
        admin.setPassword(passwordEncoder.encode(adminLoginParam.getPassword()));
        admin.setUsername(adminLoginParam.getUsername());
        admin.setEnabled(true);
        if(1==adminMapper.insert(admin)){
            return RespBean.success("????????????!");
        }
        return RespBean.error("????????????!,?????????");

    }

    /**
     * ??????????????????id
     * @return
     */
    @Override
    public RespBean maxId() {
        return null;
    }

    /**
     * ??????????????????
     * @param oldPass
     * @param password
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String password, Integer adminId) {

        Admin admin = adminMapper.selectById(adminId);
        if(!passwordEncoder.matches(oldPass,admin.getPassword())){
            return RespBean.error("?????????????????????!");
        }
        admin.setPassword(passwordEncoder.encode(password));
        if(1==adminMapper.updateById(admin)){
            return RespBean.success("??????????????????!");
        }
        return RespBean.error("??????????????????!");
    }

    /**
     * ??????????????????(??????)
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
     * ??????????????????party
     * @param token
     * @return
     */
    @Override
    public Object getPartyId(String token) {
        if(!jwtTokenUtil.canRefresh(token)){
            return Error.error("token??????");
        }
        String username = jwtTokenUtil.getUserNameFromToken(token);
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
        if(admin!=null){
            return Success.success(admin.getId());
        }
        return Error.error("????????????");
    }

    /**
     * ????????????????????????
     * @param user_id
     * @return
     */
    @Override
    public Object getAdminRoom(Integer user_id) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", user_id));
        Integer partyId = admin.getPartyId();
        if(partyId!=0)
            return Success.success(partyId);
        return Error.error("????????????????????????????????????");
    }

    /**
     * ??????????????????id
     * @param id
     */
    @Override
    public RespBean getAdminInfoById(Integer id) {
        Admin admin = null;
        admin=adminMapper.selectById(id);
        admin.setPassword("");
        if(admin!=null){
            return RespBean.success("????????????",admin);
        }
        return RespBean.error("????????????");
    }

    /**
     * ??????
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
            return RespBean.success("????????????!");
        }
        return RespBean.error("????????????");
    }

    /**
     * ????????????
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
            return RespBean.success("????????????");
        }
        return RespBean.error("????????????");
    }

    /**
     * ??????
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
            return RespBean.success("????????????!");
        }
        return RespBean.error("????????????");
    }

    /**
     * ????????????
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
            return RespBean.success("????????????");
        }
        return RespBean.error("????????????");


    }

    /**
     * ??????????????????
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
            return RespBean.success("????????????!",url);
        }
        return RespBean.error("????????????");
    }

    /**
     * ????????????
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
            return RespBean.error("???????????????????????????????????????");
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
            return RespBean.success("????????????",map);
        }
        return RespBean.error("????????????");
    }

    /**
     * ????????????
     * @param adminId
     * @param id
     * @param chooseWhat
     * @return
     */
    @Override
    public RespBean deleteFile(Integer adminId, Integer id, Integer chooseWhat) {
        Admin admin = adminMapper.selectById(adminId);
        if(!admin.getIsExpert()){
            return RespBean.error("?????????????????????????????????");
        }
        int result=0;
        if(chooseWhat==0){
            Video video = videoMapper.selectById(id);
            if(video.getAdminId()!=adminId){
                return RespBean.error("??????????????????????????????????????????");
            }
            result = videoMapper.deleteById(id);
        }else if(chooseWhat==1){
            Passage passage = passageMapper.selectById(id);
            if(passage.getAdminId()!=adminId){
                return RespBean.error("??????????????????????????????????????????");
            }
            result=passageMapper.deleteById(id);
        }
        if(result==1){
            return RespBean.success("????????????");
        }
        return RespBean.error("????????????");
    }




}

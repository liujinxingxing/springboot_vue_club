package com.club.core.service;

import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;
import com.club.base.util.StringUtil;
import com.club.core.mapper.DiscussMapper;
import com.club.core.model.Discuss;
import com.club.core.model.Notice;
import com.club.framework.mybatis.helper.PageHelper;
import com.club.framework.mybatis.service.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * DiscussService的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Service
public class DiscussService extends AbstractService<Discuss> {

    @Autowired
    protected DiscussMapper discussMapper;

    public Notice save(Discuss discuss, User user) {
        String uuid = discuss.getUuid();
        if(StringUtil.isNotEmpty(uuid)){
            if(updateByPrimaryKeySelective(discuss, user) > 0){
                return discuss;
            }
            throw new FrameworkException(ErrorCode.NO_UPDATE);
        }
        discuss.setUuid(IdGenerator.generateId(Discuss.class.getName()));
        discuss.setCreateTime(new Date());
        if(insertSelective(discuss, user) > 0){
            return discuss;
        }
        throw new FrameworkException(ErrorCode.NO_UPDATE);
    }

    public List<Discuss> list(String keyword,Page page, User user) {
        PageHelper.startPage(page);
        return discussMapper.list(StringUtil.keyword(keyword));
    }


    public int top(List<String> ids, int mode,User user) {
        int ret = 0;
        if(CollectionUtils.isNotEmpty(ids)){
            for (String id : ids) {
                Discuss discuss = new Discuss();
                discuss.setMode(mode);
                discuss.setUuid(id);
                ret += updateByPrimaryKeySelective(discuss);
            }
        }
        return ret;
    }

    @Transactional
    public int delete(List<String> ids,User user) {
        int ret = 0;
        if(CollectionUtils.isNotEmpty(ids)){
            Example example = new Example(Discuss.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn(Discuss.FIELD_UUID,ids);
            if(!user.isAdmin()){
                criteria.andEqualTo(Discuss.FIELD_UPDATE_USER_ID,user.getAccount());
            }
            ret = deleteByExample(example);
        }
        return ret;
    }

}

package com.club.core.service;

import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;
import com.club.base.util.StringUtil;
import com.club.core.mapper.NoticeMapper;
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
 * NoticeService的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Service
public class NoticeService extends AbstractService<Notice> {

    @Autowired
    protected NoticeMapper noticeMapper;

    public Notice save(Notice discuss, User user) {
        String uuid = discuss.getUuid();
        if(StringUtil.isNotEmpty(uuid)){
            if(updateByPrimaryKeySelective(discuss, user) > 0){
                return discuss;
            }
            throw new FrameworkException(ErrorCode.NO_UPDATE);
        }
        discuss.setUuid(IdGenerator.generateId(Notice.class.getName()));
        discuss.setCreateTime(new Date());
        if(insertSelective(discuss, user) > 0){
            return discuss;
        }
        throw new FrameworkException(ErrorCode.NO_UPDATE);
    }

    public List<Notice> list(String keyword,int mode,Page page, User user) {
        PageHelper.startPage(page);
        if(mode > 2 || mode < 0){
            mode = -1;
        }
        return noticeMapper.list(StringUtil.keyword(keyword),mode);
    }

    @Transactional
    public int delete(List<String> ids, User user) {
        int ret = 0;
        if(CollectionUtils.isNotEmpty(ids)){
            Example example = new Example(Notice.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn(Notice.FIELD_UUID,ids);
            if(!user.isAdmin()){
                criteria.andEqualTo(Notice.FIELD_UPDATE_USER_ID,user.getAccount());
            }
            ret = deleteByExample(example);
        }
        return ret;
    }

    public int push(List<String> ids, int mode, User user) {
        int ret = 0;
        if(CollectionUtils.isNotEmpty(ids)){
            for (String id : ids) {
                Notice notice = new Notice();
                notice.setMode(mode);
                notice.setUuid(id);
                ret += updateByPrimaryKeySelective(notice,user);
            }
        }
        return ret;
    }
}

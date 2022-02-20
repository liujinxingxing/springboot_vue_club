package com.club.core.service;

import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;
import com.club.base.util.StringUtil;
import com.club.core.bean.ContentEnum;
import com.club.core.mapper.ContentMapper;
import com.club.core.model.Content;
import com.club.core.util.ExcelUtil;
import com.club.core.vo.ContentVo;
import com.club.framework.mybatis.helper.PageHelper;
import com.club.framework.mybatis.service.AbstractService;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.util.ValidateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * ContentService的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Service
public class ContentService extends AbstractService<Content> {

    @Autowired
    protected ContentMapper contentMapper;

    public Content save(Content content, ContentEnum of, User user) {
        Content mod = selectByPrimaryKey(new Content(content.getCode(), of.getMode()));
        if (mod == null) {
            content.setMode(of.getMode());
            content.setCreateTime(new Date());
            if (insertSelective(content, user) > 0) {
                return content;
            }
            throw new FrameworkException(ErrorCode.NO_UPDATE);
        }
        mod.setDescripter(content.getDescripter());
        mod.setPositon(content.getPositon());
        mod.setPrice(content.getPrice());
        if (updateByPrimaryKeySelective(mod, user) > 0) {
            return mod;
        }
        throw new FrameworkException(ErrorCode.NO_UPDATE);
    }

    public List<ContentVo> list(String keyword, Page page, ContentEnum of, boolean flag, User user) {
        PageHelper.startPage(page);
        return contentMapper.list(StringUtil.keyword(keyword), of.getMode(), !flag, null);
    }

    @Transactional
    public int delete(List<String> ids, ContentEnum of, User user) {
        int ret = 0;
        if (CollectionUtils.isNotEmpty(ids)) {
            Example example = new Example(Content.class);
            example.createCriteria().andIn(Content.FIELD_CODE, ids).andEqualTo(Content.FIELD_MODE, of.getMode());
            ret = deleteByExample(example);
        }
        return ret;
    }

    public boolean exsist(String code, ContentEnum of) {
        Example example = new Example(Content.class);
        example.createCriteria().andEqualTo(Content.FIELD_CODE, code).andEqualTo(Content.FIELD_MODE, of.getMode());
        return contentMapper.selectCountByExample(example) > 0;
    }

    public boolean exportContent(HttpServletRequest req, HttpServletResponse resp, List<String> codes, String keyword, ContentEnum of, User user) {
        List<ContentVo> result = contentMapper.list(StringUtil.keyword(keyword), of.getMode(), false, codes);
        if (CollectionUtils.isNotEmpty(result)) {
            String[] title = {"编号", of.getMode() == 1 ? "地点" : "名称", "费用(单位：元/小时)", "备注"};
            String[][] content = new String[result.size()][title.length];
            int count = 0;
            for (Content vo : result) {
                int num = 0;
                content[count][num++] = vo.getCode();
                content[count][num++] = vo.getPositon();
                content[count][num++] = Integer.toString(vo.getPrice());
                content[count][num++] = vo.getDescripter();
                count++;
            }
            return ExcelUtil.exportData(req, resp, of.getMode() == 1 ? "导出场地" : "导出器材", title, content);
        }
        return false;
    }

    @Transactional
    public int saveListContent(List<Content> contents, ContentEnum of, User user) {
        int ret = 0;
        for (Content u : contents) {
            try {
                ValidateUtils.validate(u, Insert.class);
                if (exsist(u.getCode(), of)) {
                    continue;
                }
                u.setMode(of.getMode());
                u.setCreateTime(new Date());
                ret += insertSelective(u, user);
            } catch (Exception e) {
            }
        }
        return ret;
    }

}

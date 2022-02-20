package com.club.framework.mybatis.service;

import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.model.BaseModel;
import com.club.base.util.PageUtil;
import com.club.base.util.StringUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * 数据库抽象类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public abstract class AbstractService<T> {

    @Autowired(required = false)
    protected Mapper<T> baseMapper;

    private Class<?> entityClazz = null;

    /**
     * 获取实体类的类
     * @return java.lang.Class<?> - 实体类的类
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    protected Class<?> getEntityClass() {
        if (entityClazz == null) {
            ParameterizedType t = getParameterizedType(getClass());
            if (t == null) {
                return null;
            }
            // 数组里第一个就是子类继承父类时所用类型
            entityClazz = (Class<?>) t.getActualTypeArguments()[0];
        }
        return entityClazz;
    }

    /**
     * @param clazz - 实体类的类
     * @return java.lang.reflect.ParameterizedType - Class字节码
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */

    private ParameterizedType getParameterizedType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        // Class字节码
        Type t = clazz.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            // 因为对于T.class我们无法获取，但是这个方法就能获取到父类的参数类型，返回值为ParameterizedType
            return (ParameterizedType) t;

        }
        return getParameterizedType((Class<?>) t);
    }

    /**
     * 设置实体基本字段
     * @param entity - 实体
     * @param user   - 用户
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    private void setEntityBaseField(T entity, User user) {
        if (entity instanceof BaseModel) {
            BaseModel baseModel = (BaseModel) entity;
            baseModel.setUpdateTime(new Date());
            baseModel.setUpdateUserId(user.getAccount());
            baseModel.setUpdateUserName(user.getUserName());
        }
    }

    /**
     * 新增记录(非空也更新)
     * @param entity - 实体
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int add(T entity) {
        return insert(entity);
    }

    /**
     * 新增记录(非空也更新)，包含操作用户更新
     * @param entity - 实体
     * @param user   - 用户
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int add(T entity, User user) {
        return insert(entity, user);
    }

    /**
     * 新增记录(非空不更新)
     * @param entity - 实体
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int addSelective(T entity) {
        return insertSelective(entity);
    }

    /**
     * 新增记录(非空不更新)，包含操作用户更新
     * @param entity - 实体
     * @param user   - 用户
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int addSelective(T entity, User user) {
        return insertSelective(entity, user);
    }

    /**
     * 新增记录(非空也更新)
     * @param entity - 实体
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int insert(T entity) {
        return baseMapper.insert(entity);
    }

    /**
     * 新增记录(非空也更新)，包含操作用户更新
     * @param entity - 实体
     * @param user   - 用户
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int insert(T entity, User user) {
        setEntityBaseField(entity, user);
        return baseMapper.insert(entity);
    }

    /**
     * 新增记录(非空不更新)
     * @param entity - 实体
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int insertSelective(T entity) {
        return baseMapper.insertSelective(entity);
    }

    /**
     * 新增记录(非空不更新)，包含操作用户更新
     * @param entity - 实体
     * @param user   - 用户
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int insertSelective(T entity, User user) {
        setEntityBaseField(entity, user);
        return baseMapper.insertSelective(entity);
    }

    /**
     * 根据主键更新记录(非空也更新)
     * @param entity - 实体
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int update(T entity) {
        return baseMapper.updateByPrimaryKey(entity);
    }

    /**
     * 根据主键更新记录(非空不更新)，包含操作用户更新
     * @param entity - 实体
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int updateByPrimaryKeySelective(T entity) {
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 根据主键更新记录(非空也更新)，包含操作用户更新
     * @param entity - 实体
     * @param user   - 用户
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int update(T entity, User user) {
        setEntityBaseField(entity, user);
        return update(entity);
    }

    /**
     * 根据主键更新记录(非空不更新)，包含操作用户更新
     * @param entity - 实体
     * @param user   - 用户
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int updateByPrimaryKeySelective(T entity, User user) {
        setEntityBaseField(entity, user);
        return updateByPrimaryKeySelective(entity);
    }

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     * @param entity - 实体
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int delete(T entity) {
        return baseMapper.delete(entity);
    }

    /**
     * 根据主键删除记录
     * @param primaryKey - 主键
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int deleteByPrimaryKey(Object primaryKey) {
        return baseMapper.deleteByPrimaryKey(primaryKey);
    }

    /**
     * 根据主键删除记录
     * @param primaryKeys - 主键
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int deleteByPrimaryKeys(List<String> primaryKeys) {
        int ret = 0;
        for (String key : primaryKeys) {
            ret += baseMapper.deleteByPrimaryKey(key);
        }
        return ret;
    }


    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     * @param field - 字段
     * @param value - 值
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int deleteByField(String field, String value) {
        return deleteByFields(new String[]{field}, new String[]{value});
    }

    /**
     * 根据example进行删除
     * @param example - 样例
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int deleteByExample(Example example) {
        return baseMapper.deleteByExample(example);
    }

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     * @param fields - 字段
     * @param values - 值
     * @return int - 返回操作数量
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public int deleteByFields(String[] fields, String[] values) {
        Example example = new Example(getEntityClass());
        if (fields == null || fields.length == 0 || values == null || fields.length != values.length) {
            // 如果参数为空或者不正确，则不执行任何动作。
            return 0;
        }
        Criteria criteria = example.createCriteria();
        for (int i = 0; i < fields.length; i++) {
            criteria.andEqualTo(fields[i], values[i]);
        }
        return deleteByExample(example);
    }

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     * @param entity - 实体
     * @return T - 泛型对象
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public T selectOne(T entity) {
        return baseMapper.selectOne(entity);
    }

    /**
     * 根据主键获取
     * @param primaryKey - 主键
     * @return T - 泛型对象
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public T selectByPrimaryKey(Object primaryKey) {
        return baseMapper.selectByPrimaryKey(primaryKey);
    }

    /**
     * 根据主键获取
     * @param entity - 实体
     * @return T - 泛型对象
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public T selectByEntity(T entity) {
        return baseMapper.selectByPrimaryKey(entity);
    }

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     * @param entiry - 实体
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> select(T entiry) {
        return baseMapper.select(entiry);
    }

    /**
     * 单表分页查询,selectPage(0, 0) = selectAll()
     * @param pageNum - 第几叶
     * @param pageSize - 改叶的数量
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return baseMapper.selectAll();
    }

    /**
     * 根据实体属性作为条件进行查询，查询条件使用like 换用Page作为参数
     * @param field - 字段
     * @param value - 值
     * @param pageNum - 分页位置
     * @param pageSize - 页面容量
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByField(String field, String value, int pageNum, int pageSize) {
        return selectByFields(new String[]{field}, value, pageNum, pageSize);
    }


    /**
     * 根据实体属性作为条件进行查询，查询条件使用like 换用Page作为参数
     * @param field - 字段
     * @param value - 值
     * @param page - 分页
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByField(String field, String value, Page page) {
        return selectByFields(new String[]{field}, value, page);
    }

    /**
     * 根据实体属性作为条件进行查询，查询条件使用like
     * @param field - 字段
     * @param value - 值
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectAllByField(String field, String value) {
        return selectByField(field, value, 0, 0);
    }

    /**
     * 根据实体属性作为条件进行查询，查询条件使用like
     * @param fields - 字段
     * @param keyword - 值
     * @param pageNum - 分页位置
     * @param pageSize - 页面容量
     * @return java.util.List<T> - 泛型list
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByFields(String[] fields, String keyword, int pageNum, int pageSize) {
        Example example = new Example(getEntityClass());
        if (!StringUtils.isEmpty(keyword)) {
            String nkeyword = StringUtil.keyword(keyword);
            for (String field : fields) {
                example.or().andLike(field, nkeyword);
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        return baseMapper.selectByExample(example);
    }

    /**
     * 根据实体属性作为条件进行查询，查询条件使用like
     * @param fields - 字段
     * @param keyword - 值
     * @param page - 分页
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByFields(String[] fields, String keyword, Page page) {
        Example example = new Example(getEntityClass());
        if (!StringUtils.isEmpty(keyword)) {
            String nkeyword = StringUtil.keyword(keyword);
            for (String field : fields) {
                example.or().andLike(field, nkeyword);
            }
        }
        PageHelper.orderBy(PageUtil.getOrderCause(page.getSort(), page.getOrder(), getEntityClass()));
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return baseMapper.selectByExample(example);
    }

    /**
     * 根据实体属性作为条件进行查询，查询条件使用相等
     * @param field - 字段
     * @param value -   值
     * @param pageNum -  分页位置
     * @param pageSize - 页面大小
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByEqField(String field, String value, int pageNum, int pageSize) {
        return selectByEqFields(new String[]{field}, new String[]{value}, pageNum, pageSize);
    }

    /**
     * 根据实体属性作为条件进行查询，查询条件使用相等
     * @param field - 字段
     * @param value - 值
     * @param page - 分页
     * @return java.util.List<T> -  泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByEqField(String field, String value, Page page) {
        return selectByEqFields(new String[]{field}, new String[]{value}, page);
    }

    /**
     * @param field - 字段
     * @param value - 值
     * @return T - 泛型类
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public T selectOneByEqField(String field, String value) {
        List<T> list = selectByEqField(field, value, 1, 1);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据实体属性作为条件进行查询，查询条件使用相等
     * @param field - 字段
     * @param value - 值
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectAllByEqField(String field, String value) {
        return selectByEqField(field, value, 0, 0);
    }

    /**
     * 根据多个字段相等查询,当值比fields少时，多出的字段不加入到相等条件中
     * @param fields - 字段
     * @param values - 值
     * @param pageNum - 开始位置
     * @param pageSize - 页面大小
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByEqFields(String[] fields, String[] values, int pageNum, int pageSize) {
        Example example = new Example(getEntityClass());
        if (values != null && values.length > 0) {
            Criteria criteria = example.createCriteria();
            for (int i = 0; i < fields.length; i++) {
                if (i >= values.length) {
                    break;
                }
                criteria.andEqualTo(fields[i], values[i]);
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        return baseMapper.selectByExample(example);
    }

    /**
     * 根据多个字段相等查询,当值比fields少时，多出的字段不加入到相等条件中
     * @param fields - 字段
     * @param values - 值
     * @param page - 分页
     * @return java.util.List<T> - 泛型list
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public List<T> selectByEqFields(String[] fields, String[] values, Page page) {
        Example example = new Example(getEntityClass());
        if (values != null && values.length > 0) {
            Criteria criteria = example.createCriteria();
            for (int i = 0; i < fields.length; i++) {
                if (i >= values.length) {
                    break;
                }
                criteria.andEqualTo(fields[i], values[i]);
            }
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return baseMapper.selectByExample(example);
    }

}


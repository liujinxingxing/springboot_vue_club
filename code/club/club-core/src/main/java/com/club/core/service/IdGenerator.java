package com.club.core.service;

import com.club.core.mapper.IdGeneratorMapper;
import com.club.framework.spring.util.BeanUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * IdGenerator的描述
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class IdGenerator {

    private static final Integer INIT_ID_VALUE = 100000;

    private static IdGeneratorMapper idGeneratorMapper;

    private static IdGeneratorMapper getMapper() {
        if (idGeneratorMapper == null) {
            idGeneratorMapper = BeanUtil.getBean(IdGeneratorMapper.class);
        }
        return idGeneratorMapper;
    }

    public static String generateId(String name) {
        Integer curVal = getCurval(name);
        Integer nextVal = curVal + 1;
        if (updateVal(name, nextVal, curVal)) {
            return String.valueOf(nextVal);
        } else {
            return generateId(name);
        }
    }
    

	public static List<String> generateId(String name, int amount) {
		if(amount < 1) {
			return null;
		}
		Integer curVal = getCurval(name);
		Integer nextVal = curVal + amount;
		List<String> result = new ArrayList<String>(amount);
		if (updateVal(name, nextVal, curVal)) {
			for (int i = curVal; i < nextVal; i++) {
				result.add(String.valueOf(i + 1));
			}
			return result;
		} else {
			return generateId(name, amount);
		}
	}

    /**
     *
     * @param name
     * @return
     */
    private static Integer getCurval(String name) {
        Integer cur = getMapper().getCurVal(name);
        if (cur == null) {
            cur = INIT_ID_VALUE;
            try {
                getMapper().insertCurVal(name, cur);
            } catch (SQLException e) {
                if (e.getErrorCode() == 1) {//违反唯一约条件束
                    return getCurval(name);
                }
            }
        }
        return cur;
    }

    private static boolean updateVal(String name, Integer nextVal, Integer curVal) {
        return getMapper().updateVal(name, nextVal, curVal) > 0;
    }

}

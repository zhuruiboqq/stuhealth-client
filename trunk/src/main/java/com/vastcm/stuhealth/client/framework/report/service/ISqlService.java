package com.vastcm.stuhealth.client.framework.report.service;

import java.util.List;
import java.util.Map;

public interface ISqlService {

	/**是否存在sql要查询的内容
	 * @param sql 例如：select id from T_SAL_SaleBill where id = 'XXXX'
	 * @return 
	 */
	boolean isExist(String sql);
	
	/**是否存在sql要查询的内容。如果参数是列表作为in里的内容的不能使用此方法，例如：person.FName in (?)，不允许使用此方法，请使用:XXXX定义
	 * @param sql
	 * @param paras 参数列表，使用?定义
	 * @return
	 */
	boolean isExist(String sql, Object[] paras);
	
	/**是否存在sql要查询的内容
	 * @param sql
	 * @param paraNames 参数名称列表，使用:XXXX定义
	 * @param paraValues 参数值列表
	 * @return
	 */
	boolean isExist(String sql, String[] paraNames, Object[] paraValues);
	
	/**是否存在sql要查询的内容
	 * @param sql
	 * @param paras 参数名称---参数值，使用:XXXX定义
	 * @return
	 */
	boolean isExist(String sql, Map paras);
	
	/**查询
	 * @param sql
	 * @return
	 */
	List query(String sql);
	
	/**查询。如果参数是列表作为in里的内容的不能使用此方法，例如：person.FName in (?)，不允许使用此方法，请使用:XXXX定义
	 * @param sql
	 * @param paras 参数列表，使用?定义
	 * @return
	 */
	List query(String sql, Object[] paras);
	
	/**查询
	 * @param sql
	 * @param paraNames 参数名称列表，使用:XXXX定义
	 * @param paraValues 参数值列表
	 * @return
	 */
	List query(String sql, String[] paraNames, Object[] paraValues);
	
	/**查询
	 * @param sql
	 * @param paras 参数名称---参数值，使用:XXXX定义
	 * @return
	 */
	List query(String sql, Map paras);
	
	/**更新或删除
	 * @param sql
	 * @return The number of entities updated or deleted.
	 */
	int update(String sql);
	
	/**更新或删除。如果参数是列表作为in里的内容的不能使用此方法，例如：person.FName in (?)，不允许使用此方法，请使用:XXXX定义
	 * @param sql
	 * @param paras 参数列表，使用?定义
	 * @return The number of entities updated or deleted.
	 */
	int update(String sql, Object[] paras);
	
	/**更新或删除
	 * @param sql
	 * @param paraNames 参数名称列表，使用:XXXX定义
	 * @param paraValues 参数值列表
	 * @return The number of entities updated or deleted.
	 */
	int update(String sql, String[] paraNames, Object[] paraValues);
	
	/**更新或删除
	 * @param sql
	 * @param paras 参数名称---参数值，使用:XXXX定义
	 * @return The number of entities updated or deleted.
	 */
	int update(String sql, Map paras);
}
